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
	
	public boolean deleteFuncionarioComCompromisso(Long codigo) {
		Connection con = null;
		
		try {
			con = getConexao();
			con.setAutoCommit(false);

			deleteCompromissosDoFuncionario(con, codigo);

			boolean funcionarioExcluido = deleteFuncionario(con, codigo);

			if (!funcionarioExcluido) {
				con.rollback();
				return false;
			}

			con.commit();
			return true;

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

			throw new TechnicalException(
				"Erro ao excluir funcionário e seus compromissos.", e
			);

		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	
	private void deleteCompromissosDoFuncionario(Connection con, Long codigoFuncionario) throws SQLException {

	    String query = "DELETE FROM compromisso WHERE rowid_funcionario = ?";

	    try (PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setLong(1, codigoFuncionario);
	        ps.executeUpdate();
	    }
	}
	
	private boolean deleteFuncionario(Connection con, Long codigo) throws SQLException {

	    String query = "DELETE FROM funcionario WHERE rowid = ?";

	    try (PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setLong(1, codigo);

	        return ps.executeUpdate() > 0;
	    }
	}
}