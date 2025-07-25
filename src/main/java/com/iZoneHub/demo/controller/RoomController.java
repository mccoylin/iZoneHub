package com.iZoneHub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.repository.RoomRepository;

@Controller
public class RoomController
{
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("rooms", roomRepository.findAll());
        return "index";
    }
}
