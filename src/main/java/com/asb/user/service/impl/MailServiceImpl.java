package com.asb.user.service.impl;

import com.asb.user.model.dto.ObjectResponse;
import com.asb.user.service.IMailService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements IMailService {

    @Override
    public ResponseEntity forgotPassword(String mailTarget, String password)  {
        String subject = "Restablecer Usuario ";
        String mailBody = "Hola tu nueva contraseña es: ";
        HttpResponse<String> response = null;
        try {
            response = Unirest.post("https://arqis-cvc-dev.is.arqbs.com/arqmailgunsender/sendMailForm?idIntegration=-1&source&target")
                    .field("subject", subject)
                    .field("html", mailBody+password)
                    .field("from", "arqis-notify-sqa@arqbs.com")
                    .field("to", mailTarget).asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        if (response.getStatus() != 200) {
            ResponseEntity.BodyBuilder var10000 = ResponseEntity.status(400);
            String var10004 = response.getStatusText();
            return var10000.body(new ObjectResponse(-1, "Ha ocurrido un error al intentar enviar el correo" + var10004 + " - " + (String)response.getBody()));
        } else {
            return ResponseEntity.status(200).body(new ObjectResponse(0, "Exito al enviar el correo"));
        }
    }
}