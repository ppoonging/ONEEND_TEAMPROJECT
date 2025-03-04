package com.springboot.biz.tour.mjtour;

import com.springboot.biz.tour.mjroot.RootForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/tour")
    public String tour() {
        return "main/root/tour_form";
    }

    @GetMapping("/tour/form")
    public String rootForm(@RequestParam(value = "query", defaultValue = "") String query, Model model) {
        if(!query.isEmpty()) {
            List<Map<String, String>> searchData = tourService.search(query);
            System.out.println(searchData);
            model.addAttribute("searchData", searchData);
            model.addAttribute("query", query);
        }
        return "main/root/tour_form";
    }

//    @PostMapping("/tour/form")
//    public String rootFormPost(TourForm tourForm) {
//
//    }
}
