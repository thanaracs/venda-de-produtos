package com.example.crudcomJPA.controleDeVendas.controller;


import com.example.crudcomJPA.controleDeVendas.model.ClientePF;
import com.example.crudcomJPA.controleDeVendas.model.ItemVenda;
import com.example.crudcomJPA.controleDeVendas.model.Usuario;
import com.example.crudcomJPA.controleDeVendas.model.Venda;
import com.example.crudcomJPA.controleDeVendas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Transactional
@Controller
@Scope("request")
@RequestMapping("vendas")
public class ControllerVenda {

    @Autowired
    VendaRepository vendaRepository;

    @Autowired
    Venda venda;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ClientePFRepository clientepfrepository;

    @Autowired
    ItemVendaRepository itemVendarepository;

    @ResponseBody

    @GetMapping("/catalogo")
    public ModelAndView form(ItemVenda itemVenda) {
        ModelMap model = new ModelMap();
        model.addAttribute("produto", produtoRepository.produtos());
        model.addAttribute("itemVenda", new ItemVenda());
       // model.addAttribute("venda", new Venda());
        model.addAttribute("clientePF", clientepfrepository.clientesPF());
        venda.TotalVenda();
        return new ModelAndView("/vendas/catalogo", model);
    }
    @GetMapping("/carrinho")
    public ModelAndView from(ItemVenda itemVenda){
        ModelMap model = new ModelMap();

        model.addAttribute("itemVenda", new ItemVenda());
        // model.addAttribute("venda", new Venda());
        model.addAttribute("clientePF", clientepfrepository.clientesPF());
        venda.TotalVenda();
        return new ModelAndView("/vendas/carrinho", model);
    }

    @GetMapping("/list")
    //lista de produtos selecionaros para a compra  /venda
    public ModelAndView listar (Venda venda, ModelMap model){
        model.addAttribute("vendas", vendaRepository.vendas());
        return new ModelAndView("/vendas/list", model);
    }

    @PostMapping("/add")
    public ModelAndView add(@Valid ItemVenda itemVenda, RedirectAttributes attributes) {
        //validacao carrinho de compras
        if (itemVenda.getQuantidade() <= 0) {
            attributes.addFlashAttribute("erroQtd", "Quantidade deve ser maior que ou igual à 1");
            return new ModelAndView("redirect:/vendas/carrinho");
        }

        itemVenda.setVenda(venda);
        itemVenda.setProduto(produtoRepository.produto(itemVenda.getProduto().getIdProduto()));
        itemVenda.TotalItem();
        venda.setItemVenda(itemVenda);
        venda.TotalVenda();
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @GetMapping("/details/{idVenda}")
    public ModelAndView details(@PathVariable(value = "idVenda") int idVenda, ModelMap model) { //lista de venda

        model.addAttribute("vendas", vendaRepository.venda(idVenda));
        model.addAttribute("itemVenda", itemVendarepository.itemVendas(idVenda));
        return new ModelAndView("/vendas/detalhes", model);
    }

       @PostMapping("/addCliente")
    public ModelAndView addCliente(Venda venda, ClientePF cliente) {
        cliente.setVenda(venda);
        venda.setCliente(cliente);
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @PostMapping("/save")
    public ModelAndView save(RedirectAttributes attributes, Venda venda) {
        //ITENS VENDA VALIDACAO
        if(this.venda.getItemVenda().isEmpty()){
            attributes.addFlashAttribute("erroItem", "Carrinho está vazio");
        }
        if(!attributes.getFlashAttributes().isEmpty()){
            return new ModelAndView("redirect:/vendas/carrinho");
        }
        this.venda.setId(0);
        ClientePF clientePF = clienteLogado();
        this.venda.setLocalDate(venda.getLocalDate());
        this.venda.TotalVenda();
        this.venda.setCliente(clientepfrepository.clientePF(clientePF.getIdCliente()));
        vendaRepository.save(this.venda);
        this.venda.getItemVenda().clear();

        return new ModelAndView("redirect:/vendas/compra-finalizada");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("venda", vendaRepository.Venda(id));
        return new ModelAndView("/vendas/carrinho", model);
    }

    @PostMapping("/update")
    public ModelAndView update(Venda venda) {
        vendaRepository.update(venda);
        return new ModelAndView("redirect:/vendas/list");
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") int id){
        for(int i = 0; i < venda.getItemVenda().size(); i++){
            if(venda.getItemVenda().get(i).getProduto().getIdProduto()==(id)){
                venda.getItemVenda().remove(i);
            }
        }
        return new ModelAndView("redirect:/vendas/carrinho");
    }

    @GetMapping("/buscarfordata")
    public ModelAndView buscarfordata(@RequestParam(value = "databusca") String databusca, ModelMap model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(databusca, formatter);
        model.addAttribute("vendas", vendaRepository.vendas(data));
        return new ModelAndView("/vendas/list");
    }
    @GetMapping("/compras-usuario")
    public ModelAndView mysales(Venda venda, ModelMap model) { //lista de vendasData
        ClientePF clientePF = clienteLogado();
        model.addAttribute("vendas", vendaRepository.clienteVendas(clientePF));
        return new ModelAndView("/vendas/compras-usuario", model);
    }
    public ClientePF clienteLogado(){
        Usuario usuario = usuarioRepository.usuario(SecurityContextHolder.getContext().getAuthentication().getName());
        ClientePF clientePF = clientepfrepository.clientePF(usuario);
        return clientePF;
    }
}