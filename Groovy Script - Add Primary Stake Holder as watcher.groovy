package examples.docs
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.*
import com.atlassian.jira.issue.watchers.*;
import com.atlassian.jira.issue.fields.*;
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
def issueManager = ComponentAccessor.getIssueManager()
def watcherManager = ComponentAccessor.getWatcherManager()
def customfieldManager = ComponentAccessor.getCustomFieldManager() 
CustomField cf= customfieldManager.getCustomFieldObject("customfield_14717") 
//def issue = issueManager.getIssueByCurrentKey('ADS-57')
def puser = issue.getCustomFieldValue(cf)
if (puser != null)
{
    return watcherManager.startWatching(puser,issue)
}
