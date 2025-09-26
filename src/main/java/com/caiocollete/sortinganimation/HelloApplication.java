package com.caiocollete.sortinganimation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.util.Arrays;

public class HelloApplication extends Application {

    // --- Constantes para Configuração ---
    private static final int[] NUMEROS_INICIAIS = {4, 2, 8, 6, 1, 5, 9, 3, 7};
    private static final int TAMANHO_VETOR = NUMEROS_INICIAIS.length;
    private static final int VELOCIDADE_ANIMACAO_MS = 600;
    private static final int PAUSA_ENTRE_ETAPAS_MS = 2500;
    private static final int LARGURA_BOTAO = 40;
    private static final int ALTURA_BOTAO = 40;

    private static final String[] CORES_RUNS = {
            "#add8e6", "#f08080", "#90ee90", "#ffdab9", "#dda0dd", "#e0ffff", "#f4a460"
    };

    // --- Componentes da Interface ---
    private AnchorPane painelPrincipal;
    private Button botaoIniciar;
    private HBox containerBotoes;
    private Label labelStatus;
    private Button[] vetBotoes;

    // --- Dados para Ordenação e Animação ---
    private int[] numeros;
    private int indiceCorAtual;
    // Array para rastrear as cores dos botões de forma segura
    private String[] coresAtuaisDosBotoes;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Animação de Ordenação - Tim Sort");
        painelPrincipal = new AnchorPane();
        VBox layoutVertical = new VBox(20);
        layoutVertical.setAlignment(Pos.CENTER);
        painelPrincipal.setTopAnchor(layoutVertical, 50.0);
        painelPrincipal.setLeftAnchor(layoutVertical, 50.0);
        painelPrincipal.setRightAnchor(layoutVertical, 50.0);

        botaoIniciar = new Button("Iniciar Animação Tim Sort");
        botaoIniciar.setOnAction(e -> iniciarAnimacaoOrdenacao());

        containerBotoes = new HBox(10);
        containerBotoes.setAlignment(Pos.CENTER);

        labelStatus = new Label("Pressione 'Iniciar' para começar a ordenação.");
        labelStatus.setFont(new Font("Arial", 16));

        inicializarVetor();

        layoutVertical.getChildren().addAll(labelStatus, containerBotoes, botaoIniciar);
        painelPrincipal.getChildren().add(layoutVertical);

        Scene scene = new Scene(painelPrincipal, 800, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void inicializarVetor() {
        numeros = Arrays.copyOf(NUMEROS_INICIAIS, TAMANHO_VETOR);
        vetBotoes = new Button[TAMANHO_VETOR];
        // CORREÇÃO: Inicializa o array de rastreamento de cores
        coresAtuaisDosBotoes = new String[TAMANHO_VETOR];
        Arrays.fill(coresAtuaisDosBotoes, ""); // Começa sem estilo

        containerBotoes.getChildren().clear();

        for (int i = 0; i < TAMANHO_VETOR; i++) {
            Button btn = new Button(String.valueOf(numeros[i]));
            btn.setMinSize(LARGURA_BOTAO, ALTURA_BOTAO);
            btn.setFont(new Font(14));
            btn.setStyle("");
            vetBotoes[i] = btn;
            containerBotoes.getChildren().add(btn);
        }
        atualizarStatusLabel("Vetor Inicial: " + Arrays.toString(NUMEROS_INICIAIS));
    }

    private void iniciarAnimacaoOrdenacao() {
        botaoIniciar.setDisable(true);
        this.indiceCorAtual = 0;

        Task<Void> taskOrdenacao = new Task<>() {
            @Override
            protected Void call() {
                timSortPorEtapas();
                return null;
            }
        };

        taskOrdenacao.setOnSucceeded(e -> {
            botaoIniciar.setText("Reiniciar Animação");
            botaoIniciar.setOnAction(ev -> {
                inicializarVetor();
                iniciarAnimacaoOrdenacao();
            });
            botaoIniciar.setDisable(false);
        });

        Thread thread = new Thread(taskOrdenacao);
        thread.setDaemon(true);
        thread.start();
    }

    public void timSortPorEtapas() {
        atualizarStatusLabel("Fase 1: Ordenando as 'Runs' com Insertion Sort...");
        pausarAnimacao(PAUSA_ENTRE_ETAPAS_MS);
        insertionSort(0, 1); colorirRun(0, 1);
        insertionSort(2, 3); colorirRun(2, 3);
        insertionSort(4, 6); colorirRun(4, 6);
        insertionSort(7, 8); colorirRun(7, 8);

        atualizarStatusLabel("Runs Ordenadas e Coloridas");
        pausarAnimacao(PAUSA_ENTRE_ETAPAS_MS + 1000);

        atualizarStatusLabel("Fase 2: Juntando (Merge) as runs...");
        pausarAnimacao(PAUSA_ENTRE_ETAPAS_MS);
        merge(0, 1, 3); colorirRun(0, 3);
        merge(4, 6, 8); colorirRun(4, 8);

        atualizarStatusLabel("Runs Juntadas e com Novas Cores");
        pausarAnimacao(PAUSA_ENTRE_ETAPAS_MS + 1000);

        atualizarStatusLabel("Fase 3: Merge Final...");
        pausarAnimacao(PAUSA_ENTRE_ETAPAS_MS);
        merge(0, 3, 8); colorirRun(0, 8);

        atualizarStatusLabel("Ordenação Concluída! Vetor final: " + Arrays.toString(numeros));
    }

    private void insertionSort(int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int temp = numeros[i];
            int j = i - 1;
            setEstiloBotao(i, "-fx-background-color: orange;"); // Destaque do elemento a ser inserido
            pausarAnimacao();
            while (j >= left && numeros[j] > temp) {
                setEstiloBotao(j, "-fx-background-color: lightcoral;"); // Destaque do elemento sendo movido
                numeros[j + 1] = numeros[j];
                atualizaTextoBotao(j + 1, String.valueOf(numeros[j]));
                pausarAnimacao();
                setEstiloBotao(j, coresAtuaisDosBotoes[j]); // Restaura a cor anterior (que pode ser a da run ou vazia)
                j--;
            }
            numeros[j + 1] = temp;
            atualizaTextoBotao(j + 1, String.valueOf(temp));
            // Limpa os destaques temporários
            setEstiloBotao(i, coresAtuaisDosBotoes[i]);
            setEstiloBotao(j + 1, coresAtuaisDosBotoes[j + 1]);
        }
    }

    private void merge(int l, int m, int r) {
        int len1 = m - l + 1, len2 = r - m;
        int[] left = new int[len1];
        int[] right = new int[len2];
        for (int i = 0; i < len1; i++) left[i] = numeros[l + i];
        for (int i = 0; i < len2; i++) right[i] = numeros[m + 1 + i];

        int i = 0, j = 0, k = l;
        while (i < len1 && j < len2) {
            setEstiloBotao(l + i, "-fx-background-color: yellow; -fx-border-color: black;");
            setEstiloBotao(m + 1 + j, "-fx-background-color: yellow; -fx-border-color: black;");
            pausarAnimacao();

            if (left[i] <= right[j]) {
                numeros[k] = left[i]; i++;
            } else {
                numeros[k] = right[j]; j++;
            }

            // Restaura as cores originais antes de destacar o elemento movido
            // Usa o array `coresAtuaisDosBotoes` para ler a cor de forma segura
            if (i > 0) setEstiloBotao(l + i - 1, coresAtuaisDosBotoes[l + i - 1]);
            if (j > 0) setEstiloBotao(m + j, coresAtuaisDosBotoes[m + j]);


            atualizaTextoBotao(k, String.valueOf(numeros[k]));
            setEstiloBotao(k, "-fx-background-color: pink;");
            pausarAnimacao();
            setEstiloBotao(k, coresAtuaisDosBotoes[k]);
            k++;
        }
        while (i < len1) {
            numeros[k] = left[i]; atualizaTextoBotao(k, String.valueOf(left[i]));
            setEstiloBotao(k, "-fx-background-color: pink;"); pausarAnimacao(); setEstiloBotao(k, coresAtuaisDosBotoes[k]);
            i++; k++;
        }
        while (j < len2) {
            numeros[k] = right[j]; atualizaTextoBotao(k, String.valueOf(right[j]));
            setEstiloBotao(k, "-fx-background-color: pink;"); pausarAnimacao(); setEstiloBotao(k, coresAtuaisDosBotoes[k]);
            j++; k++;
        }
    }

    private void colorirRun(int inicio, int fim) {
        String cor = CORES_RUNS[indiceCorAtual % CORES_RUNS.length];
        indiceCorAtual++;
        String estilo = "-fx-background-color: " + cor + ";";
        for (int i = inicio; i <= fim; i++) {
            coresAtuaisDosBotoes[i] = estilo; // Atualiza nosso array de rastreamento
            setEstiloBotao(i, estilo);         // Atualiza o botão na tela
        }
    }

    private void pausarAnimacao() { pausarAnimacao(VELOCIDADE_ANIMACAO_MS); }
    private void pausarAnimacao(int milissegundos) {
        try { Thread.sleep(milissegundos); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    private void atualizarStatusLabel(String texto) { Platform.runLater(() -> labelStatus.setText(texto)); }
    private void setEstiloBotao(int index, String style) {
        if (index >= 0 && index < vetBotoes.length) { Platform.runLater(() -> vetBotoes[index].setStyle(style)); }
    }
    private void atualizaTextoBotao(int index, String texto) {
        if (index >= 0 && index < vetBotoes.length) { Platform.runLater(() -> vetBotoes[index].setText(texto)); }
    }

    public static void main(String[] args) {
        launch();
    }
}