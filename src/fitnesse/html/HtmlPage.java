// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.html;

import org.apache.velocity.VelocityContext;

import fitnesse.VelocityFactory;
import fitnesse.responders.templateUtilities.PageTitle;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.ProxyPage;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageActions;
import fitnesse.wiki.WikiPagePath;

public class HtmlPage {
  public static final String BreakPoint = "<!--BREAKPOINT-->";

  private VelocityContext velocityContext;
  
  private String templateFileName;
  private String title = "FitNesse";
  private String bodyClass;
  private PageTitle pageTitle;
  private String mainTemplate;
  private String mainContent;
  
  public WikiPageActions actions;

  public String preDivision;
  public String postDivision;

  protected HtmlPage(String templateFileName) {
    super();
    
    velocityContext =  new VelocityContext();
    this.templateFileName = templateFileName;
  }

  protected VelocityContext updateVelocityContext() throws Exception {
    velocityContext.put("title", title);
    velocityContext.put("bodyClass", bodyClass);
    makeSidebarSection();
    velocityContext.put("pageTitle", pageTitle);

    if (mainTemplate != null) {
      velocityContext.put("mainTemplate", mainTemplate);
    } else {
      velocityContext.put("mainContent", mainContent);
    }
    return velocityContext;
  }

  public void setMainTemplate(String templateName) {
    this.mainTemplate = templateName;
  }
  
  public void setMainContent(String mainContent) {
    this.mainContent = mainContent;
  }
  
  public void put(String key, Object value) {
    velocityContext.put(key, value);
  }
  
  public String html() throws Exception {
    VelocityContext context = updateVelocityContext();
    return VelocityFactory.translateTemplate(context, templateFileName);
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public void setPageTitle(PageTitle pageTitle) {
    this.pageTitle = pageTitle;
  }

  public void divide() throws Exception {
    String html = html();
    int breakIndex = html.indexOf(BreakPoint);
    preDivision = html.substring(0, breakIndex);
    postDivision = html.substring(breakIndex + BreakPoint.length());
  }

  public void setBodyClass(String clazz) {
    bodyClass = clazz;
  }
  
  public void makeSidebarSection() throws Exception {
    velocityContext.put("actions", actions);
    if (actions != null) {
      velocityContext.put("localPath", actions.getLocalPageName());
      velocityContext.put("localOrRemotePath", actions.getLocalOrRemotePageName());
      velocityContext.put("openInNewWindow", actions.isNewWindowIfRemote());
    }
  }

}
