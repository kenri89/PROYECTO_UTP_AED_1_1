package dao;

import modelo.Usuario;

public interface IUsuarioDAO {
    Usuario autenticar(String username, String password, String rolSeleccionado);
}
