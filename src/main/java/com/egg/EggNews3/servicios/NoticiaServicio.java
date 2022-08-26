package com.egg.EggNews3.servicios;

import com.egg.EggNews3.entidades.Noticia;
import com.egg.EggNews3.entidades.Periodista;
import com.egg.EggNews3.excepciones.ErrorServicio;
import com.egg.EggNews3.repositorios.NoticiaRepositorio;
import com.egg.EggNews3.repositorios.PeriodistaRepositorio;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;  //para la imagen

import net.iharder.Base64; //****OJO!!!*** librería de Maven Repository no la que trae java.util


@Service
public class NoticiaServicio {
    
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    
    
    @Transactional
    public void crearNoticia(String titulo, String cuerpo, MultipartFile imagen, String idCreador) throws ErrorServicio, IOException{
        
        validar(titulo, cuerpo, imagen);
             
        Optional<Periodista> respuesta =  periodistaRepositorio.findById(idCreador);
        Periodista periodista = new Periodista();
        if(respuesta.isPresent()){
            periodista = respuesta.get();
        } else{
            throw new ErrorServicio("Periodista inexistente");
        }
        
        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        //importar la librería de mvnrepository (ihader)
        //--> en el throwssss agregar IOException que tiran estos métodos
        noticia.setImagen(Base64.encodeBytes(imagen.getBytes())); //obtiene los bytes de la imagen y los convierte en String
        noticia.setFechaPublicacion(new Date());
        noticia.setCreador(periodista);
        
        noticiaRepositorio.save(noticia);
    }
    
    public List<Noticia> mostrarNoticiasOrdenadas(){
        List<Noticia> noticias = noticiaRepositorio.ordenarPorFecha();
        return noticias;
    }
    
    public Noticia obtenerNoticia(Long idNoticia){
           
        return noticiaRepositorio.getOne(idNoticia);  
    }
    
    public Noticia mostrarNoticia(Long idNoticia){
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        Noticia noticia =new Noticia();
        if(respuesta.isPresent()){
            noticia = respuesta.get();
        }
        return noticia;
    }
        
    @Transactional
    public void modificarNoticia(Long id, String titulo, String cuerpo, MultipartFile imagen) throws ErrorServicio, IOException{
        
        validar(titulo, cuerpo, imagen);
        
        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(id);
        
        if(respuestaNoticia.isPresent()){
            Noticia noticia = respuestaNoticia.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);  
            noticia.setImagen(Base64.encodeBytes(imagen.getBytes()));;
            noticiaRepositorio.save(noticia); 
        }      
    }
    
    
    @Transactional
    public void eliminarNoticia(Long idNoticia){
        
        Optional<Noticia> respuestaNoticia = noticiaRepositorio.findById(idNoticia);
        if(respuestaNoticia.isPresent()){
            noticiaRepositorio.deleteById(idNoticia);
        }
    }
    
    
    public void validar(String titulo, String cuerpo, MultipartFile imagen) throws ErrorServicio{
        
        if(titulo == null || titulo.isEmpty()){
            throw new ErrorServicio("El título no puede ser nulo o vacío");
        }
        if(cuerpo == null || cuerpo.isEmpty()){
            throw new ErrorServicio("El cuerpo de la noticia no puede ser nulo o vacío");
        }
        
        if(imagen.isEmpty()){
            throw new ErrorServicio("Debe adjuntar una imagen");
        }
    }
}
