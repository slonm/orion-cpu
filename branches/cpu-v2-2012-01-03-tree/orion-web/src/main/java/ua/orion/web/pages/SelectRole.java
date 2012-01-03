package ua.orion.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.orion.cpu.core.security.entities.ActiveDirectoryPrincipal;
import ua.orion.cpu.core.security.services.ThreadRole;

public class SelectRole {
    
    @Inject
    private ThreadRole thRole;
    
    public String getRole() {
        return thRole.getRole();
    }
    
    public void setRole(String role) {
        thRole.setRole(role);
    }
    
    public SelectModel getRoleModel() {
        return new AbstractSelectModel() {
            
            @Override
            public List<OptionGroupModel> getOptionGroups() {
                return null;
            }
            
            @Override
            public List<OptionModel> getOptions() {
                List<OptionModel> optionModelList = new ArrayList<OptionModel>();
                PrincipalCollection pc = SecurityUtils.getSubject().getPrincipals();
                
                if (pc != null) {
                    ActiveDirectoryPrincipal adp = pc.oneByType(ActiveDirectoryPrincipal.class);
                    if (adp != null) {
                        Set<String> roles = adp.getRoles();
                        for (final String _role : roles) {
                            if (_role.startsWith("kis/")) {
                                optionModelList.add(new AbstractOptionModel() {
                                    
                                    @Override
                                    public String getLabel() {
                                        return _role;
                                    }
                                    
                                    @Override
                                    public Object getValue() {
                                        return _role;
                                    }
                                });
                            }
                        }
                    }
                }
                return optionModelList;
            }
        };
    }
    
    public Object onActionFromRoleForm() {
        return "";
    }
}
