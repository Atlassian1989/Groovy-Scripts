import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.issue.IssueEvent
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.event.type.EventType
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.security.JiraAuthenticationContext
import com.atlassian.jira.user.ApplicationUser

def event = event as IssueEvent
Issue issue = event.getIssue()
ProjectManager projectManager = ComponentAccessor.getProjectManager()
ApplicationUser assignee = projectManager.getDefaultAssignee(issue.getProjectObject(), issue.getComponents())
MutableIssue issueObject = issue as MutableIssue
issueObject.setAssignee(assignee)
IssueManager issueManager = ComponentAccessor.getIssueManager()
JiraAuthenticationContext authenticationContext = ComponentAccessor.getJiraAuthenticationContext()
ApplicationUser loggedInUser = authenticationContext.getLoggedInUser()
issueManager.updateIssue(loggedInUser, issueObject, new MyEventDispatchOption(), true)

class MyEventDispatchOption implements EventDispatchOption {
    public MyEventDispatchOption()
    {}

    public boolean isEventBeingSent()
    {
        return true
    }

    public Long getEventTypeId()
    {
        return EventType.ISSUE_UPDATED_ID
    }
}