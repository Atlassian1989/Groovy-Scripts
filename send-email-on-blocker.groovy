import com.atlassian.jira.issue.Issue
import com.atlassian.mail.Email
import com.atlassian.mail.server.MailServerManager
import com.atlassian.mail.server.SMTPMailServer
import org.apache.log4j.Category
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.watchers.IssueWatcherAccessor
import com.atlassian.jira.issue.watchers.WatcherManager
//import com.atlassian.jira.user.util.*;
import com.atlassian.jira.user.util.UserUtil
import com.atlassian.jira.user.UserUtils
 
//log = Category.getInstance("com.onresolve.jira.groovy.example.SendEmailOnBlocker")
def issueManager = ComponentAccessor.getIssueManager() 
def userManager = ComponentAccessor.getUserManager()
def wm = ComponentAccessor.getWatcherManager()
//MailServerManager mailServerManager = ComponentAccessor.getMailServerManager()
//SMTPMailServer mailServer = mailServerManager.getDefaultSMTPMailServer()
def issue = issueManager.getIssueByCurrentKey('ADS-71')
//IssueWatcherAccessor iwa = ComponentAccessor.getComponent(IssueWatcherAccessor.class)
//Locale en = new Locale("en");
//Collection musers = userManager.getUsers()
//return iwa.getWatchers(issue,en)
def users= wm.getCurrentWatcherUsernames(issue)
for (user in users)
{
      MailServerManager mailServerManager = ComponentAccessor.getMailServerManager()
      SMTPMailServer mailServer = mailServerManager.getDefaultSMTPMailServer()
   
  if (mailServer) 
    {
      
        //return mailServer
        Email email = new Email("${user}@gogoair.com") // Set the TO address, optionally CC and BCC
        email.setSubject("Added as Watcher on  ${issue}") // todo: check the subject value
        String content = "You are being notified as you have been added as a watcher for this issue";  //TODO: Set email's body.
        email.setBody(content)
        mailServer.send(email)
    }

   else 
    {
    log.error "No SMTP mail server defined"
    }
    
}