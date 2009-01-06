/*
 * replaceTextWithField.java
 *
 * Created on February 4, 2008, 12:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;
import org.bungeni.ooo.ooDocFieldSet;

/**
 *
 * @author Administrator
 */
public class searchAndReplace implements Command {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(searchAndReplace.class.getName());
 
    /** Creates a new instance of replaceTextWithField */
    public searchAndReplace() {
    }

    public boolean execute(Context context) throws Exception {
        boolean bRet = false;
        try {
        BungeniFormContext formContext = (BungeniFormContext) context;
        log.debug("executing: searchAndReplace");
        //IBungeniForm iForm = formContext.getBungeniForm();
        //ooDocFieldSet fieldSet = formContext.getFieldSets().get(0);
        String searchFor = (String)formContext.popObjectFromFieldSet("search_for");
        String replaceWith = (String) formContext.popObjectFromFieldSet("replacement_text");
        bRet = CommonActions.action_searchAndReplace(formContext.getOoDocument(), searchFor, replaceWith);
        } catch (Exception ex) {
            log.error("Command:searchAndReplace : "+ ex.getMessage());
            bRet = true;
        } finally {
            return !bRet;
        }
    }
    
}
