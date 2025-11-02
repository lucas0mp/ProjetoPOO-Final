package controller;

import dao.PacienteDAO;
import java.util.List;
import model.Paciente;
import view.PacienteView;

public class PacienteController {
    
    private PacienteDAO dao;
    private PacienteView view;

    public PacienteController() {
        this.dao = new PacienteDAO();
        this.view = new PacienteView();
    }
    
    public void iniciar() {
        int opcao;
        
        do {
            opcao = view.exibirMenu();
            
            switch (opcao) {
                case 1:
                    cadastrarPaciente();
                    break;
                case 2:
                    listarPacientes();
                    break;
                case 3:
                    atualizarPaciente();
                    break;
                case 4:
                    excluirPaciente();
                    break;
                case 0:
                    view.exibirMensagem("Saindo do sistema...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida!");
            }
            
        } while (opcao != 0);
    }
    
    private void cadastrarPaciente() {
        view.exibirMensagem("--- Cadastro de Novo Paciente ---");
        Paciente p = view.obterDadosPaciente(null);
        dao.salvar(p);
    }
    
    private void listarPacientes() {
        List<Paciente> pacientes = dao.listarTodos();
        view.listarPacientes(pacientes);
    }
    
    private void atualizarPaciente() {
        view.exibirMensagem("--- Atualização de Paciente ---");
        listarPacientes(); // Mostra a lista para o usuário saber qual ID
        int id = view.obterIdPacienteParaAtualizar();
        if (id == -1) return;

        // Simples: Pede todos os dados novamente
        // (Uma versão melhor buscaria o paciente pelo ID primeiro)
        Paciente p = view.obterDadosPaciente(null); 
        p.setId_paciente(id); // Seta o ID para o UPDATE
        
        dao.atualizar(p);
    }
    
    private void excluirPaciente() {
        view.exibirMensagem("--- Exclusão de Paciente ---");
        listarPacientes(); // Mostra a lista
        int id = view.obterIdPacienteParaExcluir();
        if (id == -1) return;
        
        dao.excluir(id);
    }
}