package br.ufsm.csi.cursinho.model;

public enum TipoUsuario {
    ADMIN,
    PROFESSOR,
    ALUNO;

    public static TipoUsuario fromString(String value) {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.name().equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Invalid TipoUsuario value: " + value);
    }
}

