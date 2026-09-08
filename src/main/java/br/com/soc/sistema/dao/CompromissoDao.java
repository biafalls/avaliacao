package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoDao extends Dao{
	private static final String QUERY_SELECT = "SELECT "
			+ "c.rowid AS id, "
			+ "f.rowid AS id_funcionario, "
			+ "f.nm_funcionario AS nm_funcionario, "
			+ "a.rowid AS id_agenda, "
			+ "a.nm_agenda AS nm_agenda, "
			+ "c.dt_compromisso AS data, "
			+ "c.hr_compromisso AS horario "
			+ "FROM compromisso c "
			+ "INNER JOIN funcionario f "
			+ "ON c.rowid_funcionario = f.rowid "
			+ "INNER JOIN agenda a "
			+ "ON c.rowid_agenda = a.rowid ";
	
	public void insertCompromisso(CompromissoVo compromissoVo) {
		String query = "INSERT INTO compromisso (rowid_funcionario, rowid_agenda, dt_compromisso, hr_compromisso) VALUES "
				+ "	(?, ?, ?, ?)";
		
		try(Connection con = getConexao();
		    PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setLong(1, compromissoVo.getFuncionario().getRowid());
			ps.setLong(2, compromissoVo.getAgenda().getRowid());
			ps.setDate(3, Date.valueOf(compromissoVo.getData()));
			ps.setTime(4, Time.valueOf(LocalTime.parse(compromissoVo.getHorario())));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao inserir compromisso",e);
		}
	}
	
	public boolean updateCompromisso(CompromissoVo compromissoVo) {
		String query = "UPDATE compromisso SET rowid_funcionario = ?, rowid_agenda = ?, dt_compromisso = ?, hr_compromisso = ? WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)) {
			
			ps.setLong(1, compromissoVo.getFuncionario().getRowid());
			ps.setLong(2, compromissoVo.getAgenda().getRowid());
			ps.setDate(3, Date.valueOf(compromissoVo.getData()));
			ps.setTime(4, Time.valueOf(LocalTime.parse(compromissoVo.getHorario())));
			ps.setLong(5, compromissoVo.getRowid());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao editar compromisso",e);
		} 
	}
	
	public boolean deleteCompromisso(Long codigo) {
		String query = "DELETE FROM compromisso WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
				
				ps.setLong(1, codigo);
				return ps.executeUpdate() > 0;
			} catch (SQLException e) {
				throw new TechnicalException("Erro ao excluir compromisso",e);
			}
	}
	
	public void deleteCompromissosPorFuncionario(Long codigoFuncionario) {
	    String query = "DELETE FROM compromisso WHERE rowid_funcionario = ?";

	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setLong(1, codigoFuncionario);
	        ps.executeUpdate();

	    } catch (SQLException e) {
	        throw new TechnicalException( "Erro ao excluir compromissos do funcionário.", e);
	    }
	}
	
	public boolean existeCompromissoPorAgenda(Long codigoAgenda) {
	    String query ="SELECT 1 FROM compromisso WHERE rowid_agenda = ? LIMIT 1";

	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setLong(1, codigoAgenda);

	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next();
	        }

	    } catch (SQLException e) {
	        throw new TechnicalException( "Erro ao verificar compromissos da agenda.", e);
	    }
	}
	public CompromissoVo findByCodigo(Long codigo) {
		String query = QUERY_SELECT + "WHERE c.rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
				
			ps.setLong(1, codigo);
				
			try(ResultSet rs = ps.executeQuery()){
					
				if (rs.next()) 
					return montarCompromisso(rs);
					
				return null;
			}
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar compromisso",e);
		}
	}
	
	public List<CompromissoVo> findAllByFuncionario(String nome) {
		 String query = QUERY_SELECT + "WHERE f.nm_funcionario LIKE ?";

		 try (Connection con = getConexao();
			  PreparedStatement ps = con.prepareStatement(query)) {
			 
			 ps.setString(1, "%" + nome + "%");

		     try (ResultSet rs = ps.executeQuery()) {
		    	 return montarListaCompromissos(rs);
		     }

		 } catch (SQLException e) {
			 throw new TechnicalException("Erro ao consultar compromissos por funcionário", e);
		}
	}
	
	public List<CompromissoVo> findAllByAgenda(String nome) {
		String query = QUERY_SELECT + "WHERE a.nm_agenda LIKE ?";

	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, "%" + nome + "%");

	        try (ResultSet rs = ps.executeQuery()) {
	            return montarListaCompromissos(rs);
	        }

	    } catch (SQLException e) {
	        throw new TechnicalException("Erro ao consultar compromissos por agenda", e);
	    }
	}
	
	public List<CompromissoVo> findAllByData(String data) {
		String query = QUERY_SELECT + "WHERE c.dt_compromisso = ?";

	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setDate(1, Date.valueOf(data));

	        try (ResultSet rs = ps.executeQuery()) {
	            return montarListaCompromissos(rs);
	        }

	    } catch (SQLException e) {
	        throw new TechnicalException("Erro ao consultar compromissos por data", e);
	    }
	}
	
	public List<CompromissoVo> findAllByHorario(String horario) {
		 String query = QUERY_SELECT + "WHERE c.hr_compromisso = ?";

		 try (Connection con = getConexao();
		      PreparedStatement ps = con.prepareStatement(query)) {
			 
			 ps.setTime(1, Time.valueOf(LocalTime.parse(horario)));
			 
			 try (ResultSet rs = ps.executeQuery()) {
				 return montarListaCompromissos(rs);
		     }

		} catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar compromissos por horário", e);
		}
	}
	
	public List<CompromissoVo> findAllCompromissos() {
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT);

			ResultSet rs = ps.executeQuery()) {
			
			return montarListaCompromissos(rs);
			
		} catch(SQLException e) {
			throw new TechnicalException("Erro ao consultar compromissos",e);
		}
	}
	
	private CompromissoVo montarCompromisso(ResultSet rs) throws SQLException {

	    CompromissoVo vo = new CompromissoVo();

	    FuncionarioVo funcionario = new FuncionarioVo();
	    funcionario.setRowid(rs.getLong("id_funcionario"));
	    funcionario.setNome(rs.getString("nm_funcionario"));

	    AgendaVo agenda = new AgendaVo();
	    agenda.setRowid(rs.getLong("id_agenda"));
	    agenda.setNome(rs.getString("nm_agenda"));

	    vo.setRowid(rs.getLong("id"));
	    vo.setFuncionario(funcionario);
	    vo.setAgenda(agenda);
	    vo.setData(rs.getDate("data").toString());
	    vo.setHorario(rs.getTime("horario").toLocalTime().toString());

	    return vo;
	}
	
	private List<CompromissoVo> montarListaCompromissos(ResultSet rs) throws SQLException {

	    List<CompromissoVo> compromissos = new ArrayList<>();

	    while (rs.next()) {
	        compromissos.add(montarCompromisso(rs));
	    }

	    return compromissos;
	}
}
