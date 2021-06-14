package com.example.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Game extends ApplicationAdapter {
    //A variavel da Largura E Altura Dispositivo
    private float larguraDispositivo;
    private float alturaDispositivo;
    //Variavel Float de movimento do passaro
    private float variacao = 0;
    private float gravidade = 0;
    private float posicaoInicialVerticalPassaro = 0;
    //Posicao do Cano
    private float posicaoCanoHorizontal;
    private float posicaoCanoVertical;
    //Varivavel para saber o espace dos canos
    private float espacoEntreCanos;
    //Posição do passaro
    private float posicaoHorizontalPassaro = 0;

    //variavel de sprite
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    //Array de Textura do passaro
    private Texture[] passaros;
    //Texturas do fundo e dos canos
    private Texture fundo;
    //Texturas dos canos baixo e topo
    private Texture canoBaixo;
    private Texture canoTopo;
    //Textura do game over
    private Texture gameOver;
    //Textura da Logo
    private Texture logoAngryBirds;
    private Texture textureMoedaOuro;
    private Texture textureMoedaPrata;

    private MoedaCoin moedaAtual;
    private MoedaCoin moedaOuro;
    private MoedaCoin moedaPrata;
    private Circle circuloMoeda;
    private float posicaoMoedaHorizontal;
    private float posicaoMoedaVertical;
    private boolean pegouMoeda;

    //Variável de Pontos
    private int pontos = 0;
    //Estado que o jogo se encontra
    private int estadoJogo = 0;
    //Pontuação máxima do jogador
    private int pontuacaoMaxima = 0;

    //Boolena para dizer se passou do cano ou nao
    private boolean passouCano = false;
    private Random random;
    //Colisor do passaro
    private Circle circuloPassaro;
    //Colisor dos canos
    private Rectangle retanguloCanoCima;
    private Rectangle retanguloCanoBaixo;

    //Texto que aparece no jogo, pontuacao e Reniciar
    BitmapFont textoPontuacao;
    BitmapFont textoReiniciar;
    BitmapFont textoMelhorPontuacao;

    //Sons do Jogo
    Sound somVoando;
    Sound somColisao;
    Sound somPontuacao;
    Sound somMoeda;

    Preferences preferencias;

	//Criando o game
    @Override
    public void create()
    {
        //Colocar as texturas
        inicializaTextura();
        //Colocar objetos na cena
        inicializaObjeto();
        criarMoeda();
    }


	//Renderiza, começa/Desenha/Fim
    @Override
    public void render()
    {
        //Verificando o Estado do Jogo
        verificaEstadoJogo();
        //Fazer a pontuação
        validaPontos();
        desenharTexturas();
        //Fazer as colisões dos objetos
        detectarColisao();
    }

    private void inicializaObjeto()
    {
        random = new Random();
        //variável para sprite do pássaro
        batch = new SpriteBatch();
        //Setando a largura e altura do dispositivo
        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        //Setando posição inicial do pássaro
        posicaoInicialVerticalPassaro = alturaDispositivo / 2;
        posicaoCanoHorizontal = larguraDispositivo;
        //Espaçamento entre os canos
        espacoEntreCanos = 350;

        //Colocando os textos, de pontuação, reniciar e a melhor pontuação
        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(10);

        textoReiniciar = new BitmapFont();
        textoReiniciar.setColor(Color.GREEN);
        textoReiniciar.getData().setScale(3);

        textoMelhorPontuacao = new BitmapFont();
        textoMelhorPontuacao.setColor(Color.RED);
        textoMelhorPontuacao.getData().setScale(3);

        // Criando cada Renderer e Retangulo
        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoCima = new Rectangle();
        retanguloCanoBaixo = new Rectangle();
        circuloMoeda = new Circle();
        //Declarando o audio para cada tipo
        somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
        somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
        somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
        somMoeda = Gdx.audio.newSound(Gdx.files.internal("CollectCoin.wav"));

        //pegando as Preferencias e declarando para cada um
        preferencias = Gdx.app.getPreferences("flappyBird");
        pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);

    }

    private void criarMoeda()
    {
        posicaoMoedaHorizontal= larguraDispositivo;
        posicaoMoedaVertical = random.nextInt((int) 300)+500;

        textureMoedaOuro = new Texture("moeda.png");
        moedaOuro = new MoedaCoin(textureMoedaOuro,10);

        textureMoedaPrata = new Texture("moedaPrata.png");
        moedaPrata = new MoedaCoin(textureMoedaPrata,5);

        moedaAtual = moedaOuro;
    }


    private void inicializaTextura()
    {
        passaros = new Texture[3];
        //Array para pegar sprites de animação do passaro
        passaros[0] = new Texture("red1.png");
        passaros[1] = new Texture("red2.png");
        passaros[2] = new Texture("red3.png");
        //Variavel de texturas, do fundo e dos canos, gameoover
        fundo = new Texture("fundo.png");

        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");
        gameOver = new Texture("game_over.png");
        logoAngryBirds = new Texture("ANGRY-BIRDS-LOGO.png");
    }

    private void detectarColisao()
    {
        //Metodo para setar a colisao do Passaro, cano baixo e cano cima
        circuloPassaro.set(50 + passaros[0].getWidth() / 2,
                posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);
        retanguloCanoBaixo.set(posicaoCanoHorizontal,
                alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight());

        retanguloCanoCima.set(posicaoCanoHorizontal,
                alturaDispositivo / 2 + espacoEntreCanos + espacoEntreCanos / 2 + posicaoCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight());

        circuloMoeda.set(posicaoMoedaHorizontal*2,posicaoMoedaVertical + moedaAtual.moedaTexture.getHeight()/2,moedaAtual.moedaTexture.getWidth()/3);
        //Boolean para saber se bateu no cano
        boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
        boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);
       boolean bateuMoeda = Intersector.overlaps(circuloPassaro,circuloMoeda);

        //Bateu no Cano  baixo ou bateu no Cano Cima
        if(bateuCanoBaixo || bateuCanoCima)
        {
            //Caso o meu estado do Jogo 1, eu toco o som de colisao e mudo o meu estado do JOgo
            if(estadoJogo == 1)
            {
                somColisao.play();
                estadoJogo = 2;
            }
        }
        //Caso bateu na Moeda, ganhe o pontos dela
       if(bateuMoeda && estadoJogo ==1 )
       {
           pontos+=moedaAtual.pontos;
           pegouMoeda = true;
           somMoeda.play();
       }


    }

    private void validaPontos()
    {
        //Verificar para ganhar pontos
        if(posicaoCanoHorizontal < 50 - passaros[0].getWidth())
        {
            if(!passouCano)
            {
                pontos++;
                passouCano = true;
                somPontuacao.play();
            }
        }
        variacao += Gdx.graphics.getDeltaTime() * 10;
        if (variacao > 3)
            variacao = 0;
    }



    private void verificaEstadoJogo()
    {
        //Boolean para setar o toque de tela
        //Estado do Jogo, para ganhar e gameover
        boolean toqueTela = Gdx.input.justTouched();
        if(estadoJogo == 0)
        {
            //Se tocar na tela a gravidade dele vai para menos 15
            if (toqueTela)
            {
                gravidade = -15;
                estadoJogo = 1;
                somVoando.play();
            }

        }
        else if (estadoJogo == 1)
        {
            if (toqueTela)
            {
                gravidade = -15;
                somVoando.play();
            }
            posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
            posicaoMoedaHorizontal -= Gdx.graphics.getDeltaTime()*200;
            if(posicaoCanoHorizontal < -canoBaixo.getHeight())
            {
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVertical = random.nextInt(400) - 200;
                passouCano = false;
            }
            if(posicaoMoedaHorizontal < -moedaAtual.moedaTexture.getHeight() || pegouMoeda)
            {
                posicaoMoedaHorizontal= larguraDispositivo;
                posicaoMoedaVertical = random.nextInt((int) 300)+500;
                int randomMoeda = random.nextInt(5);
                if(randomMoeda==1){
                    moedaAtual = moedaOuro;
                }
                else{
                    moedaAtual = moedaPrata;
                }
                pegouMoeda = false;
            }
            //Fazendo o pássaro voar com toque de tela
            if (posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

            gravidade++;
        }
        else if(estadoJogo == 2)
        {
            if(pontos > pontuacaoMaxima)
            {
                pontuacaoMaxima = pontos;
                preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
            }

            posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;

            if(toqueTela)
            {
                estadoJogo = 0;
                pontos = 0;
                gravidade = 0;
                posicaoHorizontalPassaro = 0;
                posicaoInicialVerticalPassaro = alturaDispositivo /2;
                posicaoCanoHorizontal = larguraDispositivo;
            }
        }
    }
    private void desenharTexturas()
    {
        batch.begin();

        //Colocando o fundo e o pássaro na tela
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaros[(int) variacao], 50 + posicaoHorizontalPassaro, posicaoInicialVerticalPassaro);
        batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
        batch.draw(canoTopo,posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
        textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

        if(estadoJogo == 2)
        {
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
            textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR!", larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);
            textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUAÇÃO É : " + pontuacaoMaxima +  "PONTOS!", larguraDispositivo / 2 - 300, alturaDispositivo / 2 - gameOver.getHeight() * 2);
        }else if(estadoJogo==0)
        {
            //Desenhar a logo
            batch.draw(logoAngryBirds,posicaoMoedaHorizontal/2, alturaDispositivo / 2,500,500);
        }else if(!pegouMoeda){
            batch.draw(moedaAtual.moedaTexture,posicaoMoedaHorizontal,posicaoMoedaVertical,100,100);
        }

        batch.end();
    }

    @Override
    public void dispose() {
    }
}



