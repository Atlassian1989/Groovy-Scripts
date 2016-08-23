import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.user.util.UserUtil
import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.ComponentManager
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.index.IndexException
import com.atlassian.jira.ManagerFactory
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Category
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.link.IssueLink;
//import com.googlecode.jsu.util.WorkflowUtils
 
String jqlSearch = 'project = "XXX" AND issuetype in ("XXX") AND status in ("PENDING CLOSE") ORDER BY updated DESC'
SearchService searchService = ComponentAccessor.getComponent(SearchService.class)
ComponentManager componentManager = ComponentManager.getInstance()
CommentManager commentManager = ComponentAccessor.getCommentManager()
IssueManager issueManager = ComponentAccessor.getIssueManager();
User user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
//UserUtil userUtil = ComponentAccessor.getUserUtil()
//def user2 = userUtil.getUserByKey("sysadmin")
def issueLinkManager = componentManager.getIssueLinkManager()
 
Category log = Category.getInstance("com.onresolve.jira.groovy")
log.setLevel(org.apache.log4j.Level.DEBUG)
log.debug "CLOSING ISSUES IF ALL RELATED TICKETS/SUBTAKS ARE CLOSED"
 
def String adminUserName = "user_who_close_the_issues"
def String nagComment = ""
 
List<Issue> issues = null
 
SearchService.ParseResult parseResult = searchService.parseQuery(user, jqlSearch)
if (parseResult.isValid()) {
def searchResult = searchService.search(user, parseResult.getQuery(), PagerFilter.getUnlimitedFilter())
// Transform issues from DocumentIssueImpl to the "pure" form IssueImpl (some methods don't work with DocumentIssueImps)
issues = searchResult.issues.collect {issueManager.getIssueObject(it.id)}
for ( issue in issues ){
Collection<Issue> subTasks = issue.getSubTaskObjects();
def issueType = ComponentAccessor.getConstantsManager().getIssueTypeObject("Bug");
int closed = 0;
int total = 0;
for (Issue i : subTasks) {
log.debug "subtask: ${i.getId()}, ${i.getKey()}. DETECTED"
total++;
if (i.getStatusObject().getName().equals("Closed")) {
log.debug "subtask: ${i.getId()}, ${i.getKey()}. IS CLOSED"
closed++;
}
}
//log.debug "yeah: " + total + " " + closed;
if ( total == closed ) {
total = 0;
closed = 0;
//We can search all the links of the parent ticket if are closed
List<IssueLink> allOutIssueLink = issueLinkManager.getOutwardLinks(issue.id);
for (Iterator<IssueLink> outIterator = allOutIssueLink.iterator(); outIterator.hasNext();) {
total++;
IssueLink issueLink = (IssueLink) outIterator.next();
def linkedIssue = issueLink.getDestinationObject();
log.debug "linked: ${linkedIssue.getId()}, ${linkedIssue.getKey()}. DETECTED"
if (linkedIssue.getStatusObject().getName().equals("Closed")) {
log.debug "linked Closed: ${linkedIssue.getId()}, ${linkedIssue.getKey()}. IS CLOSED"
closed++;
}
 
}
if ( total == closed ) {
//We can CLOSE the parent ticket
nagComment = "Issue closed automatically."
log.debug "issue: ${issue.getId()}, ${issue.getKey()} commented."
commentManager.create(issueManager.getIssueObject(issue.getKey()), adminUserName, nagComment, true)
//SET STATUS CLOSED
MutableIssue myIssue = issue
myIssue.setStatusId("6") //CLOSED
myIssue.setResolutionId("8") //DONE
myIssue.store()
}
}
 
}
log.debug " FINISH CLOSING ISSUES"
 
} else {
log.debug("Invalid JQL: " + jqlSearch);
}
