package com.zcj.kotlin.ddu.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

/**
 * @Since2017/9/20 ZhaCongJie@HF
 */
@Controller
class EmailController {
    @GetMapping("email/index")
    @ResponseBody
    fun index(): ModelAndView {
        return ModelAndView("index");
    }
}