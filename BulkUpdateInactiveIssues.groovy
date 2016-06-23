//To find all issues that haven't been updated in x days, where the assignee is an inactive account, 
//And then reset those issues to unassigned.

import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.index.IssueIndexManager
import com.atlassian.jira.util.ImportUtils
 
def indexManager = ComponentAccessor.getComponent(IssueIndexManager)
 
boolean wasIndexing = ImportUtils.isIndexIssues();
ImportUtils.setIndexIssues(true);
 
try {
    // do something to issue object and store
    issue.store()
}
finally {
    indexManager.reIndex(issue);
    ImportUtils.setIndexIssues(wasIndexing);
}
