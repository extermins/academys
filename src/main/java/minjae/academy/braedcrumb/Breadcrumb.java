package minjae.academy.braedcrumb;

import org.springframework.stereotype.Component;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@Component
public class Breadcrumb {
    public Model addBreadcrumb(Model model,String breadcrumb,String breadcrumbWord){
        model.addAttribute("breadcrumb",breadcrumb);
        model.addAttribute("breadcrumbWord",breadcrumbWord);
        return model;
    }
}
