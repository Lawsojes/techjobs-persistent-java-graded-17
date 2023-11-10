package org.launchcode.techjobs.persistent.controllers;

import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("skills")
public class SkillController {
    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("title", "Skills");
        model.addAttribute("skills", skillRepository.findAll());
        return "skills/index";
    }
    @GetMapping
    public String displaySkills(@RequestParam(required = false) Integer skillId, Model model) {

        if (skillId == null) {
            model.addAttribute("title", "All Skills");
            model.addAttribute("skills", skillRepository.findAll());
        } else {
            Optional<Skill> result = skillRepository.findById(skillId);
            if (result.isEmpty()) {
                model.addAttribute("title", "Invalid Employer ID: " + skillId);
            } else {
                Skill skill = result.get();
                model.addAttribute("title", "Employer Id: " + skill.getId());
                model.addAttribute("skills", skill.getName());
            }
        }

        return "skills/index";
    }


    @GetMapping("add")
    public String displayAddSkillForm(Model model) {
        model.addAttribute(new Skill());
        return "skills/add";
    }

    @PostMapping("add")
    public String processAddSkillForm(@ModelAttribute @Valid Skill newSkill,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "skills/add";
        }
        skillRepository.save(newSkill);
        return "redirect:/skills";
    }

    @GetMapping("view/{skillId}")
    public String displayViewSkill(Model model, @PathVariable int skillId) {

        Optional<Skill> optionalSkill = skillRepository.findById(skillId);
        if (!optionalSkill.isEmpty()) {
            Skill employer = optionalSkill.get();
            model.addAttribute("skill", employer);
            return "/skills/view";
        } else {
            return "redirect:../";
        }

    }
}