package com.zcj.kotlin.ddu.controller

import com.zcj.kotlin.ddu.domain.Email
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.Message.RecipientType.CC
import javax.mail.Session.getDefaultInstance
import javax.mail.internet.*
import javax.servlet.http.HttpServletRequest


/**
 * @Since2017/9/20 ZhaCongJie@HF
 */
@Controller
class EmailController {
    @Autowired
    private val env: Environment? = null

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
            sendEmail(email)
        } catch (e: Exception) {
            e.printStackTrace()
            model.set("msg", "发送失败")
            return ModelAndView("sucess")
        }
        model.set("msg", "发送成功")
        return ModelAndView("sucess")
    }

    private fun sendEmail(email: Email) {
        println(env?.getProperty("mail.smtp.port"))
        val subject = getSubject()
        val sendHtml = getHtml()
        val receiveUserList = getReceiveUserList()
        val fileSign = copyFile(File("D://report//templateEmail.xlsx"), File("D://report"), getEmailName())
        val file = File("D://report//" + getEmailName())
        if (!fileSign) {
            return
        }
        if (file.exists()) {
            execuFile(email, file)
        }
        doSendHtmlEmail(subject, sendHtml, receiveUserList, file)
    }

    private fun execuFile(email: Email, file: File) {
        val fileIn = FileInputStream(file)
        val wb = XSSFWorkbook(fileIn)
        val sheet = wb.getSheetAt(0)
        // 时间
        val row = sheet.getRow(2) //行
        val cellDate = row.getCell(0)
        cellDate.setCellValue(Date())
        // 今日工作
        val cellToday = row.getCell(2)
        cellToday.setCellValue(email.todayReport)
        // 明日工作
        val rowTomorrow = sheet.getRow(4)
        val cellTomorrow = rowTomorrow.getCell(2)
        cellTomorrow.setCellValue(email.tomorrowReport)
        // 备注
        val rowRemarks = sheet.getRow(6)
        val cellRemarks = rowRemarks.getCell(2)
        cellRemarks.setCellValue(email.remarks)

        val fileOut = FileOutputStream(file)
        wb.write(fileOut)
        fileOut.flush()
        fileOut.close()
    }

    private fun doSendHtmlEmail(subject: String?, sendHtml: String?, receiveUserList: List<String>, file: File) = try {
        val properties = Properties()
        properties.put("mail.smtp.host", env?.getProperty("mail.smtp.host"))
        properties.put("mail.smtp.port", env?.getProperty("mail.smtp.port"))
        properties.put("mail.smtp.user", env?.getProperty("mail.smtp.user"))
        properties.put("mail.smtp.auth", env?.getProperty("mail.smtp.auth"))
        properties.put("mail.smtp.debug", env?.getProperty("mail.smtp.debug"))
        val session = getDefaultInstance(properties, null)
        session.debug = true
        val message = MimeMessage(session)
        message.setFrom(InternetAddress("zhacongjie@qgutech.com"))
        val address = arrayOfNulls<InternetAddress>(receiveUserList.size)
        for (ex in receiveUserList.indices) {
            val exAddress = InternetAddress(receiveUserList[ex])
            address[ex] = exAddress
        }
        val CCList = env?.getProperty("email.CC.address")?.split(",")!!
        val CCAddress = arrayOfNulls<InternetAddress>(CCList.size)
        for (ex in CCList.indices){
            val exAddress = InternetAddress(CCList[ex])
            CCAddress[ex] = exAddress
        }
        message.setRecipients(Message.RecipientType.TO, address)
        message.setRecipients(Message.RecipientType.CC, CCAddress)
        message.subject = subject
        message.sentDate = Date()
        val messagePart: BodyPart = MimeBodyPart()
        messagePart.setText(sendHtml)
        val attachmentPart = MimeBodyPart()
        val fileDataSource = FileDataSource(file)
        attachmentPart.dataHandler = DataHandler(fileDataSource)
        attachmentPart.fileName = MimeUtility.encodeWord(file.name)
        val multiPart = MimeMultipart()
        multiPart.addBodyPart(messagePart)
        multiPart.addBodyPart(attachmentPart)
        message.setContent(multiPart)
        message.saveChanges()
        val transport = session.getTransport("smtp")
        transport.connect(properties.get("mail.smtp.host").toString(), 25, "zhacongjie@qgutech.com", "qgutech.123")
        transport.sendMessage(message, message.allRecipients)
        transport.close()
    } finally {
    }

    private fun getReceiveUserList(): List<String> {
        val list = env?.getProperty("email.receive.address")?.split(",")
        return list!!;
    }
    private fun getCCUserList():List<String>{
        val list = env?.getProperty("email.CC.address")?.split(",")
        return list!!;
    }

    private fun getHtml(): String? {
        return "发登 你好：\n" +
                "附件为今日工作，请查收。\n" +
                "\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n" +
                " \n" +
                "查从杰（软件工程师）\n" +
                "合肥青谷信息科技有限公司\n" +
                "好好学习，天天向上！！\n" +
                "ADD:  安徽省合肥市政务区天鹅湖万达广场2号写字楼1708室（230071）\n" +
                "PHONE:18855581594\n" +
                "EMAIL:zhacongjie@qgutech.com \n" +
                " \n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n"
    }


    private fun getEmailName(): String? {
        return getSubject() + "-.xlsx"
    }

    private fun getSubject(): String? {
        val date = Date()
        val sdf = SimpleDateFormat("MMdd");
        return "ELP日报【查从杰】-17" + sdf.format(date)
    }

    private fun copyFile(srcFile: File?, destDir: File?, newFileName: String?): Boolean {
        if (!destDir?.exists()!!) {
            destDir.createNewFile()
        }

        if (srcFile == null || !srcFile.exists()) {
            println("源文件不存在")
            return false
        }
        val fcin = FileInputStream(srcFile).channel
        val fcout = FileOutputStream(File(destDir, newFileName)).channel
        fcin.transferTo(0, fcin.size(), fcout)
        fcin.close()
        fcout.close()
        return true
    }
}






