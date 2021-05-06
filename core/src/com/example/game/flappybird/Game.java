package com.example.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture passaro;
	Texture fundo;

	private float larguraDispositivo;
	private float alturaDispositivo;

	private int movimentaY=0;
	private int movimentaX=0;

	//Criação de tudo do Game
	@Override
	public void create () {
		batch = new SpriteBatch();
		//Criando o Fundo
		fundo = new Texture("fundo.png");
		//Criando o Passaro, referencia sendo feita pela pasta Assets
		passaro = new Texture("passaro1.png");

		//Pegando a largura do Dispositivo
		larguraDispositivo = Gdx.graphics.getWidth();
		//Pegando altura do Dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();
	}

	//Renderiza, começa/Desenha/Fim
	@Override
	public void render () {
		batch.begin();
        //Desenha o fundo, na posição X e Y, usando a variavel que pegou a larguras e alturas do Dispositivo
		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);
		//Desenha o passaro e colocando variavels que pode ser alterada por um contador
		batch.draw(passaro,movimentaX,movimentaY);

		//Contador para fazer o Passaro movimenta horizontalmente
		movimentaX++;
		//movimentaY++;

		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
