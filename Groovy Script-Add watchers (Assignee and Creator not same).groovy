package examples.docs
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.*
import com.atlassian.jira.issue.watchers.*;
def issueManager = ComponentAccessor.getIssueManager()
def watcherManager = ComponentAccessor.getWatcherManager()
def issue = issueManager.getIssueByCurrentKey('ADS-58')
def ouser  =  issue.getCreator()
def auser =  issue.getAssignee()

if (auser != ouser)
{
   return watcherManager.startWatching(auser,issue)
}
