package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.enums.PeriodoDisponivel;
import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.vo.AgendaVo;

public class AgendaDao extends Dao {
	private static final String QUERY_SELECT_TUDO =
	        "SELECT rowid id, nm_agenda nome, periodo_disponivel periodo FROM agenda ";
	
	public void insertAgenda(AgendaVo agendaVo) {
		String query = "INSERT INTO agenda (nm_agenda, periodo_disponivel) VALUES (?, ?)";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, agendaVo.getNome());
			ps.setString(2, agendaVo.getPeriodoDisponivel().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao inserir agenda",e);
		}
	}
	
	public boolean updateAgenda(AgendaVo agendaVo) {
		String query = "UPDATE agenda SET nm_agenda = ?, periodo_disponivel = ? WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, agendaVo.getNome());
			ps.setString(2, agendaVo.getPeriodoDisponivel().toString());
			ps.setLong(3, agendaVo.getRowid());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao editar agenda",e);
		} 
	}
	
	public boolean deleteAgenda(Long codigo) {
		String query = "DELETE FROM agenda WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setLong(1, codigo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao excluir agenda",e);
		}
	}
	
	public AgendaVo findByCodigo(Long codigo) {
		String query = QUERY_SELECT_TUDO + "WHERE rowid = ?"; 
				
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setLong(1, codigo);
			
			try(ResultSet rs = ps.executeQuery()){
				AgendaVo vo = null;
				
				while (rs.next()) {
					vo = new AgendaVo();
					vo.setRowid(rs.getLong("id"));
					vo.setNome(rs.getString("nome"));
					vo.setPeriodoDisponivel(
							PeriodoDisponivel.valueOf(rs.getString("periodo")));
				}
				
				return vo;
			}
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar agenda",e);
		}
	}
	
	public List<AgendaVo> findAllByNome(String nome) {
		String query = QUERY_SELECT_TUDO + "WHERE nm_agenda LIKE ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, "%"+nome+"%");
			
			try(ResultSet rs = ps.executeQuery()){
				AgendaVo vo = null;
				List<AgendaVo> agendas = new ArrayList<>();
				
				while (rs.next()) {
					vo = new AgendaVo();
					vo.setRowid(rs.getLong("id"));
					vo.setNome(rs.getString("nome"));
					vo.setPeriodoDisponivel(
							PeriodoDisponivel.valueOf(rs.getString("periodo")));
					agendas.add(vo);
				}
				return agendas;
			}
			
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar agendas",e);
		}	
	}
	
	public List<AgendaVo> findAllByPeriodo(PeriodoDisponivel periodo) {
		String query = QUERY_SELECT_TUDO + "WHERE periodo_disponivel = ?"; 
		
		try(Connection con = getConexao();
				PreparedStatement ps = con.prepareStatement(query)){
				
				ps.setString(1, periodo.name());
				
				try(ResultSet rs = ps.executeQuery()){
					AgendaVo vo = null;
					List<AgendaVo> agendas = new ArrayList<>();
					
					while (rs.next()) {
						vo = new AgendaVo();
						vo.setRowid(rs.getLong("id"));
						vo.setNome(rs.getString("nome"));
						vo.setPeriodoDisponivel(
								PeriodoDisponivel.valueOf(rs.getString("periodo")));
						agendas.add(vo);
					}
					return agendas;
				}
				
			} catch (SQLException e) {
				throw new TechnicalException("Erro ao consultar agendas",e);
			}	
	}
	
	public List<AgendaVo> findAllAgendas() {
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(QUERY_SELECT_TUDO);
				
			ResultSet rs = ps.executeQuery()) {
			
			AgendaVo vo = null;
			List<AgendaVo> agendas = new ArrayList<>();
			
			while(rs.next()) {
				vo = new AgendaVo();
				vo.setRowid(rs.getLong("id"));
				vo.setNome(rs.getString("nome"));
				vo.setPeriodoDisponivel(
						PeriodoDisponivel.valueOf(rs.getString("periodo")));
				
				agendas.add(vo);
			}
			return agendas;
			
		} catch(SQLException e) {
			throw new TechnicalException("Erro ao consultar agendas",e);
		}
	}
}
