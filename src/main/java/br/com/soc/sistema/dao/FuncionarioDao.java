package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioDao extends Dao {
	
	public void insertFuncionario(FuncionarioVo funcionarioVo) {
		String query = "INSERT INTO funcionario (nm_funcionario) values (?)";
		
		try(Connection con = getConexao();
			PreparedStatement  ps = con.prepareStatement(query)){
			
			ps.setString(1, funcionarioVo.getNome());
			ps.executeUpdate();
		}catch (SQLException e) {
			 throw new TechnicalException("Erro ao inserir funcionário",e);
		}
	}
	
	public boolean updateFuncionario(FuncionarioVo funcionarioVo) {
		String query = "UPDATE funcionario SET nm_funcionario = ? WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement  ps = con.prepareStatement(query)){
			
			ps.setString(1, funcionarioVo.getNome());
			ps.setLong(2, funcionarioVo.getRowid());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao editar funcionário",e);
		} 
	}
	
	public boolean deleteFuncionario(Long codigo) {
		String query = "DELETE FROM funcionario WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement  ps = con.prepareStatement(query)) {
			
			ps.setLong(1, codigo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao excluir funcionário",e);
		}
	}
	
	public boolean findSeFuncionarioTemCompromisso(Long codigo) {
		String query = "SELECT f.rowid FROM funcionario f INNER JOIN compromisso c "
				+ "ON f.rowid = c.rowid_funcionario WHERE f.rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setLong(1, codigo);
			
		    try (ResultSet rs = ps.executeQuery()) {
		    	return rs.next();
	        }
		}catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar compromissos do funcionário",e);
		}
	}
	
	public FuncionarioVo findByCodigo(Long codigo){
		String query = "SELECT rowid id, nm_funcionario nome FROM funcionario "+
								"WHERE rowid = ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setLong(1, codigo);
			
			try(ResultSet rs = ps.executeQuery()){
				FuncionarioVo vo =  null;
				
				while (rs.next()) {
					vo = new FuncionarioVo();
					vo.setRowid(rs.getLong("id"));
					vo.setNome(rs.getString("nome"));	
				}
				return vo;
			}
		}catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar funcionário",e);
		}
	}
	
	public List<FuncionarioVo> findAllByNome(String nome) {
		String query = "SELECT rowid id, nm_funcionario nome FROM funcionario "+
						"WHERE nm_funcionario like ?";
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, "%"+nome+"%");
			
			try(ResultSet rs = ps.executeQuery()){
				FuncionarioVo vo =  null;
				List<FuncionarioVo> funcionarios = new ArrayList<>();
				
				while (rs.next()) {
					vo = new FuncionarioVo();
					vo.setRowid(rs.getLong("id"));
					vo.setNome(rs.getString("nome"));	
					
					funcionarios.add(vo);
				}
				return funcionarios;
			}
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar funcionários",e);
		}	
	}
	
	public List<FuncionarioVo> findAllFuncionarios() {
		String query = "SELECT rowid id, nm_funcionario nome FROM funcionario";
		
		try(Connection con = getConexao();
			PreparedStatement  ps = con.prepareStatement(query);
				
			ResultSet rs = ps.executeQuery()){
			
			FuncionarioVo vo =  null;
			List<FuncionarioVo> funcionarios = new ArrayList<>();
			while (rs.next()) {
				vo = new FuncionarioVo();
				vo.setRowid(rs.getLong("id"));
				vo.setNome(rs.getString("nome"));	
				
				funcionarios.add(vo);
			}
			return funcionarios;
		} catch (SQLException e) {
			throw new TechnicalException("Erro ao consultar funcionários",e);
		}
	}
}