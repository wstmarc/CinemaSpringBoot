package fr.laerce.cinema2.web;

import fr.laerce.cinema2.dao.FilmsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class MainController {

    @Autowired
    FilmsDAO filmsDAO;

    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("nom","Karl");
        model.addAttribute("films",filmsDAO.films());
        return "index";
    }

    @GetMapping("/film/{id}")
    public String detail(Model model, @PathVariable("id") String id){
        Integer idFilm = Integer.parseInt(id);
        model.addAttribute("film", filmsDAO.getById(idFilm));
        return "detail";
    }

//TODO Essayer d'externaliser le controlleur
/*    @GetMapping("affiche/{id}")
    public String affiche(Model model, @PathVariable("id") String id){
        Integer idFilm = Integer.parseInt(id);
        String nomimagefilm = filmsDAO.getById(idFilm).getAfficheNom();
        model.addAttribute("nomfilm", nomimagefilm);
        return "affiche";
    }*/
    //on créer une methode affiche qui est mapper /affiche/id avec id, le nom de l'affiche du film
    @GetMapping("/affiche/{id}")
    public void affiche (HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        
//merci patrick
        /////////////////////////////////////////////////////
        //on copie colle le code du prof et on adapte
        // Chemin absolu de l'image
        String url="C:\\Users\\CDI\\Desktop\\affiches\\";
        //chemin relatif
        String filename =url+id;
        // Type mime associé à l'image d'après le nom de fichier
        //on a besoin de request d'ou request et response dans les parametre de la methode
        //on recupere a partir de la request le context du servlet et la methode getmine
        String mime = request.getServletContext ().getMimeType (filename);
        //gestion du null
        if (mime == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        //1on defini le type dans response
        response.setContentType(mime);
        //on créée lefichier voulu
        File file = new File(filename);
        //2 on definit la Longeur de la réponse
        response.setContentLength((int)file.length());
        //on converti le fichier en fileinputstream
        FileInputStream in = new FileInputStream(file);
        //on recupere l'objet outputstream de response qui est bien configurer grace a 1 et 2
        OutputStream out = response.getOutputStream();

        // Copie le contenu du fichier vers le flux de sortie(demander rien je comprend pas a partir de là)
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }
}
