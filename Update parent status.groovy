import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.opensymphony.workflow.WorkflowContext;
import com.atlassian.jira.config.SubTaskManager
import com.atlassian.jira.workflow.WorkflowTransitionUtil;
import com.atlassian.jira.workflow.WorkflowTransitionUtilImpl;
import com.atlassian.jira.util.JiraUtils;
import com.atlassian.jira.issue.status.Status

def issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey('SVP-60')
if (issue.getSummary() == 'Automate back ups')

{
// Get the details of the user executing the workflow transition for the issue change history
//String currentUser = ((WorkflowContext) transientVars.get("context")).getCaller();
WorkflowTransitionUtil workflowTransitionUtil = (WorkflowTransitionUtil) JiraUtils.loadComponent(WorkflowTransitionUtilImpl.class);
 //return 1;
// Get the ID of the parent issue
MutableIssue parent = issue.getParentObject() as MutableIssue
workflowTransitionUtil.setIssue(parent);
workflowTransitionUtil.setAction(21); // 91 is the ID of the Global Dev In Progress transition
workflowTransitionUtil.validate();
workflowTransitionUtil.progress(); 
}
else
    {
        return 0;
    }