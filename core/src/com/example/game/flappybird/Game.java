package com.example.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture[] passaros;
	Texture fundo;
	Texture[] canosAlto;
	Texture[] canosBaixo;
	private int movimentaY=0;
	private int movimentaX=0;

	private float larguraDispositivo;
	private float alturaDispositivo;
	private float variacao=0;
	private float gravidade = 0;
	private float posicaoIniciaVerticalPassaro=0;



	//Variaves do Cano
	private StateCano enumCano;
	//a velocidade do Cano
	private float speedCano;
	//Altura do Cano
	private float alturaCano;
	//posicao do Cano que vai ser a variavel que vai ser alterada, que vai fazer o Cano andar
	private float posicaoXCano;

	//Criação de tudo do Game
	@Override
	public void create () {
		batch = new SpriteBatch();
		//Criando o Fundo
		fundo = new Texture("fundo.png");
		criandoCano();
		criandoPassaro();
		pegandoTela();

		startCano();
	}

	//Criando as Texture dos Cano
	private void criandoCano() {
		canosAlto = new Texture[2];
		canosAlto[0] = new Texture("cano_topo_maior.png");
		canosAlto[1]= new Texture("cano_topo.png");

		canosBaixo = new Texture[2];
		canosBaixo[0] = new Texture("cano_baixo_maior.png");
		canosBaixo[1]= new Texture("cano_baixo.png");
	}

	//Pegando o Tamanho da Tela e suas configurações
	private void pegandoTela() {
		//Pegando a largura do Dispositivo
		larguraDispositivo = Gdx.graphics.getWidth();
		//Pegando altura do Dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();
		speedCano = 5;
		posicaoXCano = larguraDispositivo;
		posicaoIniciaVerticalPassaro = alturaDispositivo/2;
	}

	private void criandoPassaro() {
		passaros = new Texture[3];
		//Criando o Passaro, referencia sendo feita pela pasta Assets
		passaros[0]= new Texture("passaro1.png");
		passaros[1]= new Texture("passaro2.png");
		passaros[2]= new Texture("passaro3.png");
	}

	//Renderiza, começa/Desenha/Fim
	@Override
	public void render () {
		batch.begin();

		if(variacao>3)
			variacao=0;

		boolean toqueTela = Gdx.input.justTouched();
		if(Gdx.input.justTouched()){
			gravidade=-25;
		}

		if(posicaoIniciaVerticalPassaro>0|| toqueTela)
			posicaoIniciaVerticalPassaro = posicaoIniciaVerticalPassaro-gravidade;

		desenhandoNaTela();

		//Contador para fazer o Passaro movimenta horizontalmente
		movimentaX++;
		posicaoXCano-=speedCano;
		gravidade++;

		batch.end();
	}
    //Desenhando na Tela Mais animação do Personagem
	private void desenhandoNaTela() {
		//Desenha o fundo, na posição X e Y, usando a variavel que pegou a larguras e alturas do Dispositivo
		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);


		//Desenha o passaro e colocando variavels que pode ser alterada por um contador
		batch.draw(passaros[(int) variacao],0,posicaoIniciaVerticalPassaro);
		//Iniciando Cada Cano a cada vez que seja no final do cenario
		if(posicaoXCano<=(larguraDispositivo/larguraDispositivo)-100){
			startCano();
		};
		//Movimentação do Cano
		movimentandoCano();
		variacao+= Gdx.graphics.getDeltaTime() * 10;
	}

	//Pego um Cano aleatorio que esta em uma combinação de um Enum, tambem pega o tamanho aleatorio do Cano e a Posição Inicial que ele vai aparecer
	private void startCano() {
		enumCano = StateCano.getCanoRandom();
		tamanhoRandomCano();
		posicaoXCano= larguraDispositivo;
	}

//Movimentando o Cano pelo Valor do EnumCano que foi decidido Aleatoriamente
	private void movimentandoCano() {

		switch (enumCano){
			case canoAlto:
				desenhandoCano(1,false);
				break;
			case canoAltoMaior:
				desenhandoCano(0,false);
				break;
			case canoBaixo:
				desenhandoCano(1,true);
				break;
			case canoBaixoMaior:
				desenhandoCano(0,true);
				break;
			case canoBaixoeAlto:
				desenhandoCano(1,true);
				batch.draw(canosAlto[1],posicaoXCano,alturaDispositivo/2 +(alturaDispositivo/alturaCano) );
				break;
			case canoAltoeBaixo:
				desenhandoCano(0,false);
				batch.draw(canosBaixo[1],posicaoXCano,0 - (alturaDispositivo/alturaCano));
				break;
			default:
				break;
		}

	}
  //Tamanho Aleatorio do Cano para fazer ele ocupar a tela para as combinações de 2 canos
	private void tamanhoRandomCano(){
		Random random = new Random();
		alturaCano = random.nextInt((7 - 3) + 1) + 3;
	}


//Metodo para desenhar o Cano e a movimentação dele,
	private void desenhandoCano(int numeroCano,boolean baixo){
		if(baixo){
			batch.draw(canosBaixo[numeroCano],posicaoXCano,0);
		}
		else{
			batch.draw(canosAlto[numeroCano],posicaoXCano,alturaDispositivo/2);
		}
	}

	@Override
	public void dispose () {

	}

	//Enum do Cano
	public enum StateCano{
		canoAltoMaior(0),
		canoBaixoMaior(1),
		canoBaixo(2),
		canoAlto(3),
		canoAltoeBaixo(4),
		canoBaixoeAlto(5);

		//Valor do Cano
		private final int valor;
		StateCano(int valorCano)
		{
			valor = valorCano;
		}
		//Pegando um cano Aleotariamente e devolvendo
		public static StateCano getCanoRandom(){
			Random random = new Random();
			return values() [random.nextInt(values().length)];
		}
		public int getValor()
		{
			return valor;
		}

		}
	}


