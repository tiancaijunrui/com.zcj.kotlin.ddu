package com.zcj.kotlin.ddu.controller

import ch.qos.logback.core.net.LoginAuthenticator
import com.zcj.kotlin.ddu.domain.Email
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.Session.getDefaultInstance
import javax.mail.internet.*
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
            createExcel(email)
            sendEmail()
        } catch (e: Exception) {
            e.printStackTrace()
            model.set("msg", "发送失败")
            return ModelAndView("sucess")
        }
        model.set("msg", "发送成功")
        return ModelAndView("sucess")
    }

    private fun sendEmail() {
        val subject = getSubject()
        val sendHtml = getHtml()
        val receiveUserList = getReceiveUserList()
        val file = File("D://email.xlsx")
        doSendHtmlEmail(subject, sendHtml, receiveUserList, file)
    }

    private fun doSendHtmlEmail(subject: String?, sendHtml: String?, receiveUserList: List<String>, file: File) = try {
        val sendUserName = InternetAddress("zhacongjie")
        val properties = Properties()
        properties.put("mail.smtp.host", "smtp.qgutech.com")
        properties.put("mail.smtp.port", "25")
        properties.put("mail.smtp.user","zhacongjie@qgutech.com")
        properties.put("mail.smtp.auth","true")
        properties.put("mail.smtp.debug","true")
//            todo
//        val auth = Authenticator()
        val session = getDefaultInstance(properties, null)
        session.debug = true
        val message = MimeMessage(session)
        message.setFrom(InternetAddress("zhacongjie@qgutech.com"))
        val address = arrayOfNulls<InternetAddress>(receiveUserList.size)
        for (ex in receiveUserList.indices){
            val exAddress = InternetAddress(receiveUserList[ex])
            address[ex] = exAddress
        }
        message.setRecipients(Message.RecipientType.TO,address)
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
        transport.connect(properties.get("mail.smtp.host").toString(),25,"zhacongjie@qgutech.com","qgutech.123")
        transport.sendMessage(message,message.allRecipients)
        transport.close()
    } finally {
//            transport.close();
    }

    private fun getReceiveUserList(): List<String> {
        val list = ArrayList<String>()
//        list.add("zhacongjie@qgutech.com")
        list.add("984453536@qq.com")
        return list
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

    private fun createExcel(email: Email) {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("sheet1")
        for (i in 1..5) {
            val row = sheet.createRow(i)
            for (j in 5..10) {
                val cell = row.createCell(j)
                cell.setCellValue("Cell" + i + " " + j)
            }
        }
//            val fileOut = FileOutputStream(System.getProperty("user.dir") + "/src/main/resources/download/email.xlsx")
        val fileOut = FileOutputStream("D://email.xlsx")
        wb.write(fileOut)
        fileOut.flush()
        fileOut.close()
    }

    private fun getEmailName(): String? {
        val date = Date()
        val sdf = SimpleDateFormat("ELP日报【查从杰】-17MMDD-.xlsx");
        return sdf.format(date)
    }

    private fun getSubject(): String? {
        val date = Date()
        val sdf = SimpleDateFormat("MMDD");
        return "ELP日报【查从杰】-17"+sdf.format(date)
    }
}



