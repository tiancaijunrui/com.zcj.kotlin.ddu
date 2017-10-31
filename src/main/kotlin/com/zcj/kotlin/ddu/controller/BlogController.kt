package com.zcj.kotlin.ddu.controller

import com.github.stuxuhai.jpinyin.PinyinFormat
import com.github.stuxuhai.jpinyin.PinyinHelper
import com.zcj.kotlin.ddu.domain.Blog
import com.zcj.kotlin.ddu.domain.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.io.*
import javax.servlet.http.HttpServletRequest

/**
 * @Since2017/9/30 ZhaCongJie@HF
 */
@Controller
@RestController
class BlogController {
    @Autowired
    private val env: Environment? = null
    var path = env?.getProperty("blog.physical.path") + env?.getProperty("blog.relative.path")

    @GetMapping("blog/index")
    @ResponseBody
    fun index(model: Model): ModelAndView {
        val blog: Blog = listFile()
        model.set("blog", blog)
        return ModelAndView("blog/index");
    }

    @GetMapping("blog/editFileName/{fileName}")
    @ResponseBody
    fun editFileName(@PathVariable fileName: String, model: Model, request: HttpServletRequest): ModelAndView {
        val file: File = getFile(fileName)
        val inputStreamReader: InputStreamReader = InputStreamReader(FileInputStream(file))
        val br = BufferedReader(inputStreamReader)
        var line: String? = br.readLine()
        if (StringUtils.isEmpty(line)) {
            br.close()
            inputStreamReader.close()
            model.set("content", getHeadText(fileName))
            return ModelAndView("blog/edit")
        }
        val content: StringBuilder = StringBuilder()
        while (line != null) {
            line += "\r\n"
            content.append(line)
            line = br.readLine()
        }
        br.close()
        inputStreamReader.close()
        model.set("content", content.toString().trim())
        return ModelAndView("blog/edit")
    }

    @PostMapping("blog/deleteFile")
    fun deleteFile(request: HttpServletRequest): JsonResult {
        val jsonResult = JsonResult();
        val fileName: String = request.getParameter("fileName")
        if (StringUtils.isEmpty(fileName)) {
            jsonResult.status = "ERROR"
            jsonResult.message = "文件名称不存在~~"
            return jsonResult;
        }
        val file: File = getFile(fileName)
        file.delete()
        jsonResult.status = "SUCCESS"
        return jsonResult;
    }

    private fun getHeadText(title :String): String {
        return "---\n" +
                "title: "+title+"\n" +
                "tags: test\n" +
                "categories: test\n" +
                "---"
    }

    private fun getFile(fileName: String): File {
        val path = env?.getProperty("blog.physical.path") + env?.getProperty("blog.relative.path")
        var filePath: String = path + "\\" + fileName
        if (!filePath.contains(".md")){
            filePath += ".md"
        }
        val file: File = File(filePath)
        return file
    }

    @PostMapping("blog/save/{fileName}")
    fun save(@PathVariable fileName: String, model: Model, request: HttpServletRequest): JsonResult {
        val jsonResult = JsonResult();
        val content: String = request.getParameter("content")
        val file: File = getFile(fileName)
        val fop: FileOutputStream = FileOutputStream(file)
        if (!file.exists()) {
            file.createNewFile()
        }
        fop.write(content.toByteArray())
        fop.flush()
        fop.close()
        jsonResult.status = "SUCCESS";
        return jsonResult;
    }

    private fun listFile(): Blog {
        val path = env?.getProperty("blog.physical.path") + env?.getProperty("blog.relative.path")
        val directory = File(path)
        val blog = Blog()
        if (directory.isDirectory) {
            val fileList = directory.listFiles()
            val fileNameList: ArrayList<String> = blog.fileNameList
            fileList.mapTo(fileNameList) { it.name }
        }
        return blog
    }

    @PostMapping("blog/addFile")
    fun addFile(request: HttpServletRequest): JsonResult {
        val jsonResult: JsonResult = JsonResult()
        var fileName: String = request.getParameter("fileName")
        if (!StringUtils.isEmpty(fileName)) {
            fileName = PinyinHelper.convertToPinyinString(fileName, "", PinyinFormat.WITHOUT_TONE)
            val file: File = getFile(fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            jsonResult.status = "SUCCESS";
            return jsonResult;
        }
        jsonResult.status = "ERROR";
        return jsonResult;
    }
}

