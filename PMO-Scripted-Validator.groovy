import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.*
import com.atlassian.jira.issue.watchers.*;
import com.atlassian.jira.issue.fields.*;
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
def issueManager = ComponentAccessor.getIssueManager()
//def watcherManager = ComponentAccessor.getWatcherManager()
def customfieldManager = ComponentAccessor.getCustomFieldManager() 
CustomField cfcat = customfieldManager.getCustomFieldObject("customfield_12904")
CustomField cfcl= customfieldManager.getCustomFieldObject("customfield_12006")
CustomField cffal= customfieldManager.getCustomFieldObject("customfield_12007")
CustomField cfccc= customfieldManager.getCustomFieldObject("customfield_16204")
CustomField cffacc= customfieldManager.getCustomFieldObject("customfield_16205")
def issue = issueManager.getIssueByCurrentKey('PR-648')
if (issue.getCustomFieldValue(cfcat).toString() == "Capability")
{
    return true;
	//Write Logic for Workflow Transition 
	//(if P3 transition condition)
	//then make cfccc and cffacc as Mandatory
}

else
{
  return false;
  // Write logic to make cfcl and cffal as Mandatory
}