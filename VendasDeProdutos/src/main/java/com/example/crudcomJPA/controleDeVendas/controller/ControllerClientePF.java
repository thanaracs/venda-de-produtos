package com.example.crudcomJPA.controleDeVendas.controller;

import com.example.crudcomJPA.controleDeVendas.model.ClientePF;
import com.example.crudcomJPA.controleDeVendas.model.Usuario;
import com.example.crudcomJPA.controleDeVendas.repository.ClientePFRepository;
import com.example.crudcomJPA.controleDeVendas.repository.RoleRepository;
import com.example.crudcomJPA.controleDeVendas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Transactional
@Controller
@RequestMapping("clientes")
public class ControllerClientePF {

    @Autowired
    ClientePFRepository repository;

    @Autowired
    UsuarioRepository usuariorepository;

    @Autowired
    ClientePFRepository clientePFrepository;

    @Autowired
    RoleRepository rolerepository;

    @GetMapping("/cadastrar-cliente")
    public ModelAndView form(ClientePF clientePF) {
        return new ModelAndView("/autenticacao/cadastrousuario");
    }

    @GetMapping("/form-user")
    public ModelAndView formuser(ClientePF clientePF) {
        return new ModelAndView("/clientes/form-user");
    }

    @GetMapping("/form-adm")
    public ModelAndView formadm(ClientePF clientePF) {
        return new ModelAndView("/clientes/form-adm");
    }

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("clientePF", repository.clientesPF());
        return new ModelAndView("/clientes/list", model);
    }

    public ClientePF insereDadosClientes(ClientePF clientePF){
        Usuario usuario = new Usuario();
        usuario = clientePF.getUsuario();
        usuario.setPassword(new BCryptPasswordEncoder().encode(clientePF.getUsuario().getPassword()));
        usuario.getRoles().add(rolerepository.role(2L));
        clientePF.setUsuario(usuario);
        usuario.setCliente(clientePF);
        usuariorepository.save(usuario);
        return clientePF;
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid ClientePF clientePF, BindingResult result, RedirectAttributes attributes) { //salva usuario
        if(result.hasErrors()) {
            attributes.addFlashAttribute("erroCadastro", "Informe Nome e CPF!");
            return cadastrousuario(clientePF);
        }
        if(clientePF.getUsuario().getLogin().equals(null) ||
                clientePF.getUsuario().getLogin().isEmpty() ||
                clientePF.getUsuario().getPassword().equals(null) ||
                clientePF.getUsuario().getPassword().isEmpty()) {
            attributes.addFlashAttribute("erroCadastro", "Informe login e senha!");
            return new ModelAndView("redirect:/clientes/cadastrousuario");
        }
        repository.save(insereDadosClientes(clientePF));  //cliente repository
        attributes.addFlashAttribute("cadastroUser", "Usuário cadastrado com sucesso!");
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/salvaradm")
    public ModelAndView salvaradm(@Valid ClientePF clientePF, BindingResult result, RedirectAttributes attributes) {
        if(result.hasErrors()) {
            return formadm(clientePF);
        }
        if(clientePF.getUsuario().getLogin().equals(null) ||
        clientePF.getUsuario().getLogin().isEmpty() ||
        clientePF.getUsuario().getPassword().equals(null) ||
        clientePF.getUsuario().getPassword().isEmpty()) {

            attributes.addFlashAttribute("erroLogin", "Informe login e senha!");
            return new ModelAndView("redirect:/clientes/formadm");
        }

        Usuario usuario = new Usuario();
        usuario = clientePF.getUsuario();
        usuario.setPassword(new BCryptPasswordEncoder().encode(clientePF.getUsuario().getPassword()));
        usuario.getRoles().add(rolerepository.role(1L));
        clientePF.setUsuario(usuario);
        usuario.setCliente(clientePF);
        usuariorepository.save(usuario);
        clientePFrepository.save(clientePF);
        attributes.addFlashAttribute("cadastro", "Administrador cadastrado com sucesso!");
        return new ModelAndView("redirect:/clientes/list");
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid ClientePF cliente, BindingResult result, RedirectAttributes attributes) {
        if(result.hasErrors())
            return formuser(cliente);
        //para manter o objeto com dados preenchidos
        repository.save(insereDadosClientes(cliente));
        attributes.addFlashAttribute("cadastro", "Usuário cadastrado com sucesso!");
        return new ModelAndView("redirect:/clientes/list");
    }

    @GetMapping("/remove/{idCliente}")
    public ModelAndView remove(@PathVariable("idCliente") int idCliente, RedirectAttributes attributes) {
        ClientePF clientePF = clientePFrepository.clientePF(idCliente);
        if(clientePF.getVenda().isEmpty()){
            Usuario usuario = usuariorepository.usuarioCliente(clientePF);
            usuariorepository.remove(usuario.getIdUsuario());
            clientePFrepository.remove(idCliente);
        }
        else {
            attributes.addFlashAttribute("erroExcluir", "Erro ao excluir. Esse usuário já possui compras!");
        }
        return new ModelAndView("/index");
    }

    @GetMapping("/editar")
    public ModelAndView edit(ModelMap model) {
        ClientePF clientePF = clienteLogado();
        int idCliente = clientePF.getIdCliente();
        model.addAttribute("clientePF", repository.clientePF(idCliente));
        return new ModelAndView("/clientes/editar-user", model);
    }


    public ClientePF clienteLogado(){
        Usuario usuario = usuariorepository.usuario(SecurityContextHolder.getContext().getAuthentication().getName());
        ClientePF clientePF = clientePFrepository.clientePF(usuario);
        return clientePF;
    }

    @PostMapping("/update")
    public ModelAndView update(ClientePF clientePF, RedirectAttributes attributes) {
        if(clientePF.getUsuario().getPassword().isEmpty() || clientePF.getUsuario().getLogin().isEmpty() ){
            attributes.addFlashAttribute("erroAtualizar", "Insiria sua senha!!");
            return new ModelAndView("redirect:/clientes/editar");
        }
        Usuario usuario = usuariorepository.usuarioCliente(clientePF);
        usuario.setPassword(new BCryptPasswordEncoder().encode(clientePF.getUsuario().getPassword()));
        usuario.setLogin(clientePF.getUsuario().getLogin());
        clientePF.setUsuario(usuariorepository.usuario(usuario.getLogin()));
        usuario.setCliente(clientePF);
        usuariorepository.update(usuario);
        clientePFrepository.update(clientePF);
        attributes.addFlashAttribute("atualizado", "Atualizado com sucesso!!");
        return new ModelAndView("redirect:/login?logout");
    }

    @GetMapping("/buscarforname")
    public ModelAndView buscarforname(@RequestParam(value = "nome") String nome, ModelMap model) {
        model.addAttribute("clientePF", repository.clientes(nome));
        return new ModelAndView("/clientes/list");
    }

    @GetMapping("/cadastrousuario")
    public ModelAndView cadastrousuario(ClientePF clientePF) {
        return new ModelAndView("/autenticacao/cadastrousuario");
    }


    @GetMapping("/cadastros")
    public ModelAndView cadastrar() {
        return new ModelAndView("/links/cadastros");
    }
}
