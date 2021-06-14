package com.example.game.flappybird;

import com.badlogic.gdx.graphics.Texture;

public class MoedaCoin {

    public Texture moedaTexture;
    public int pontos;

    public MoedaCoin(Texture texture, int pontos)
    {
        this.moedaTexture = texture;
        this.pontos = pontos;
    }
}
