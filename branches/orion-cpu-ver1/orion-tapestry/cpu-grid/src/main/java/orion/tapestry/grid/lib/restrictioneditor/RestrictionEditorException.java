/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.grid.lib.restrictioneditor;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class RestrictionEditorException extends Exception{
        String msg;
        public RestrictionEditorException(String _msg) {
            this.msg = _msg;
        }
        public RestrictionEditorException() {
            this.msg = null;
        }
        @Override
        public String getMessage(){
            return this.msg;
        }
}
