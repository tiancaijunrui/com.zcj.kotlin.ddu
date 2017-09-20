package com.zcj.kotlin.ddu.controller

import com.zcj.kotlin.ddu.domain.Email
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

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

    @PostMapping("email/toSendFadeng")
    @ResponseBody
    fun toSendFadeng(email: Email, model: Model, request: HttpServletRequest): ModelAndView {
        try {
            email.remarks = request.getParameter("email.remarks")
            email.todayReport = request.getParameter("email.todayReport")
            email.tomorrowReport = request.getParameter("email.tomorrowReport")
        } catch (e: Exception) {
            e.printStackTrace()
            model.set("msg", "发送失败")
            return ModelAndView()
        }
        model.set("msg", "发送成功")
        return ModelAndView("sucess");
    }
}



