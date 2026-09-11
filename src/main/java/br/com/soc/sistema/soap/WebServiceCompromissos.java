package br.com.soc.sistema.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import br.com.soc.sistema.vo.CompromissoVo;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WebServiceCompromissos {

    @WebMethod
    public CompromissoVo buscarCompromisso(Long codigo);

    @WebMethod
    public CompromissoVo[] listarCompromissos();

    @WebMethod
    public CompromissoVo[] buscarCompromissosPorFuncionario(String nome);

    @WebMethod
    public CompromissoVo[] buscarCompromissosPorAgenda(String nome);

    @WebMethod
    public CompromissoVo[] buscarCompromissosPorData(String data);

    @WebMethod
    public CompromissoVo[] buscarCompromissosPorHorario(String horario);
    
    @WebMethod
    public CompromissoVo[] buscarCompromissosEntre(String dataInicial, String dataFinal);
}