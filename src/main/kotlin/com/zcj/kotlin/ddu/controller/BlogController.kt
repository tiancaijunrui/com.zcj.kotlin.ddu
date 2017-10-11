package com.zcj.kotlin.ddu.controller

import com.zcj.kotlin.ddu.domain.Blog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.io.File
import javax.annotation.Resource

/**
 * @Since2017/9/30 ZhaCongJie@HF
 */
@Controller
class BlogController {
    @Autowired
    private val env: Environment? = null

    @GetMapping("blog/index")
    @ResponseBody
    fun index(): ModelAndView {
        val blog :Blog = listFile()
        return ModelAndView("blog/index");
    }

    private fun listFile(): Blog {
        val path = "D:\\IdeaProjects\\blog\\source\\_posts"
        val directory = File(path)
        val blog = Blog()
        if (directory.isDirectory) {
            val fileList = directory.listFiles()
            val fileNameList: ArrayList<String> = blog.fileNameList
            fileList.mapTo(fileNameList) { it.name }
        }
        return blog
    }
}

