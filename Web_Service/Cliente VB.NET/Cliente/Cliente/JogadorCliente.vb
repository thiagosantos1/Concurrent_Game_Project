Imports System.Net.Sockets
Imports System.IO
Imports System.Text
Imports System.Threading
Imports Cliente.ServicoJogo

Module JogadorCliente

    Dim jogTradutor As jogadorTradutor
    Dim jogadorClient As JogoTradutorItalianoClient

    Const TIPO_MENU_JOGO_NAO_INICIADO = 0
    Const TIPO_MENU_INICAR = 1
    Const TIPO_MENU_DESAFIO = 2
    Const TIPO_MENU_ADM = 3

    Const JOGO_INICIADO = 1
    Const JOGO_NAO_INICIADO = 0

    Const ACAO_LISTAR_JOGADORES As String = "/players"
    Const ACAO_LISTA_REGRAS As String = "/regras"
    Const ACAO_DESISTR As String = "/desistir"
    Const ACAO_INICAR As String = "/iniciar"
    Const ACAO_PLACAR As String = "/placar"
    Const ACAO_RESPONDER As String = "/responder"
    Const ACAO_PLACAR_INDIVIDUAL As String = "/placar_i"
    Const ACAO_INICIAR_ADM As String = "/c"

    Sub Main()
        Try
            inicializeVariaveis()
            obtenhaNomeJogadorERegistre()

            If jogadorClient.adminJogo = jogTradutor.idJogador Then
                exibaMenuDoJogo(TIPO_MENU_ADM)
            Else
                exibaMenuDoJogo(TIPO_MENU_INICAR)
            End If
        Catch ex As Exception
            Console.Write(ex.Message)
            Thread.Sleep(6000)
        End Try

    End Sub

    Private Sub inicializeVariaveis()
        jogTradutor = New jogadorTradutor
        jogadorClient = New JogoTradutorItalianoClient
    End Sub

    Private Sub obtenhaEProcesseAcao()
        Dim acao As String = String.Empty
        acao = Console.ReadLine()
        Select Case acao
            Case ACAO_DESISTR
                executeAcaoDesistir()
            Case ACAO_INICAR
                executeDesafio()
            Case ACAO_LISTA_REGRAS
                executeAcaoListarRegras()
            Case ACAO_RESPONDER
                executeDesafio()
            Case ACAO_LISTAR_JOGADORES
                executeAcaoListar()
            Case ACAO_PLACAR
                executeAcaoPlacar()
            Case ACAO_PLACAR_INDIVIDUAL
                executeAcaoPlacarIndividual()
            Case ACAO_INICIAR_ADM
                executeAcaoInicarAdm()
        End Select
        obtenhaEProcesseAcao()
        verificaSeClienteVirouAdministrador()
    End Sub

    Private Sub executeAcaoInicarAdm()
        If jogadorClient.getStatus < 2 Then
            Console.WriteLine("O jogo ainda não pode ser inciado, aguarde.")
        End If
        While jogadorClient.getQtdJogadores < 2
        End While
        jogadorClient.setStatus(JOGO_INICIADO)
        Console.WriteLine("O jogo foi iniciado.")
        exibaMenuDoJogo(TIPO_MENU_INICAR)
    End Sub

    Private Sub executeAcaoListarRegras()
        Console.WriteLine(jogadorClient.getRegras())
    End Sub

    Private Sub executeAcaoListar()
        Console.WriteLine(jogadorClient.listarJogadores())
    End Sub

    Private Sub executeDesafio()
        'Enqunto não estiver eliminado o desafio será lançado.
        While Not jogadorClient.jogadorEliminado(jogTradutor.idJogador) And Not jogadorClient.gameOver
            lanceDesafioEObtenhaResposta()
            If jogadorClient.getIdGanhador = jogTradutor.idJogador Then
                exibaMensagemVitoria()
                Exit While
            End If
            exibaMenuDoJogo(TIPO_MENU_DESAFIO)
        End While
        If jogadorClient.jogadorEliminado(jogTradutor.idJogador) Then
            Console.WriteLine("Você foi eliminado!")
            jogadorClient.removerJogador(jogTradutor.idJogador)
        End If
        If jogadorClient.gameOver Then
            Console.WriteLine("O jogo foi encerrado.")
        End If
    End Sub

    Private Sub verificaSeClienteVirouAdministrador()
        If jogadorClient.adminJogo = jogTradutor.idJogador Then
            Console.WriteLine("Vocé o novo administrador")
        End If
    End Sub

    Private Sub executeAcaoPlacar()
        Console.WriteLine(jogadorClient.getPlacarGeral())
    End Sub

    Private Sub executeAcaoPlacarIndividual()
        Console.WriteLine(jogadorClient.getPlacarGeral())
    End Sub

    Private Sub obtenhaNomeJogadorERegistre()

        Console.WriteLine("******************************************************************")
        Console.WriteLine("     Bem vindo ao Tradutor Italino")
        Console.WriteLine("******************************************************************")
        Console.WriteLine("Digite seu nome: ")

        jogTradutor.nome = Console.ReadLine()
        jogTradutor.idJogador = jogadorClient.setIdJogador

        If jogadorClient.getStatus = JOGO_NAO_INICIADO And jogadorClient.getQtdJogadores < 5 Then
            jogadorClient.registrar(jogTradutor)
            Console.WriteLine(jogadorClient.getRegras())
        ElseIf jogadorClient.getStatus <> JOGO_NAO_INICIADO Then
            Console.WriteLine("Jogo ja iniciado. Aguarde iniciar uma nova partida para poder entrar no jogo.")
            exibaMenuDoJogo(TIPO_MENU_JOGO_NAO_INICIADO)
        Else
            Console.WriteLine("Jogo ja possui 5 jogadores.Aguarde iniciar uma nova partida para poder entrar no jogo.")
            While jogadorClient.getStatus <> JOGO_NAO_INICIADO And jogadorClient.getQtdJogadores >= 5
            End While
            If jogadorClient.getQtdJogadores = 0 Then
                Console.WriteLine("Novo Jogo")
            Else
                Console.WriteLine("Nova partida encontrada.")
            End If
            jogadorClient.registrar(jogTradutor)
            Console.WriteLine(jogadorClient.getRegras())
        End If
    End Sub

    Private Sub lanceDesafioEObtenhaResposta()
        jogadorClient.setNewDesafioItaliano(jogTradutor.idJogador)
        Console.WriteLine(jogadorClient.lancarDesafio(jogTradutor.idJogador) + ": Significa ?")

        If jogadorClient.checarResposta(Console.ReadLine(), jogTradutor.idJogador) > 0 Then
            Console.WriteLine("***************************")
            Console.WriteLine("     Acertou, Braaavo!!!!")
            Console.WriteLine("*************************** ")
        Else
            Console.WriteLine("Errou, essa nao e a traducao correta!")
        End If
    End Sub

    Private Sub executeAcaoDesistir()

        jogadorClient.removerJogador(jogTradutor.idJogador)
        Console.WriteLine("*****************************************")
        Console.WriteLine("Obrigado pela sua participação Jogo Desconectado")
        Console.WriteLine("*****************************************")

    End Sub

    Private Sub exibaMensagemVitoria()
        Console.WriteLine("**********************************************************")
        Console.WriteLine(" Parabéns, você foi o ganhador!!!")
        Console.WriteLine("**********************************************************")
    End Sub

    Private Function jogadorEhAdm() As Boolean
        Return jogadorClient.adminJogo = jogTradutor.idJogador
    End Function

    Private Sub exibaMenuDoJogo(ByVal tipoMenu As Integer)
        Select Case tipoMenu
            Case TIPO_MENU_JOGO_NAO_INICIADO
                Console.WriteLine("Digite '/players' para listar os jogadores conectados ao jogo,")
                Console.WriteLine("'/regras' para listar as regras do jogo ou,")
                Console.WriteLine("'/desistir' para desistir do jogo")
                Console.WriteLine("******************************************************************")
                If Not jogadorEhAdm() Then
                    While Not jogoPodeSerIniciado() 
                    End While
                    Console.WriteLine("O jogo pode ser iniciado.")
                    exibaMenuDoJogo(TIPO_MENU_INICAR)
                End If
            Case TIPO_MENU_INICAR
                    exibaCabecaMenu()
                    Console.WriteLine("Digite '/iniciar' para comecar, ")
                    Console.WriteLine("/players' para listar os jogadores, ")
                    Console.WriteLine("'/regras' para listar as regras do jogo ou,")
                    Console.WriteLine("'/desistir' para desistir do jogo")
                    Console.WriteLine("******************************************************************")     
            Case TIPO_MENU_DESAFIO
                    exibaCabecaMenu()
                    Console.WriteLine("Digite /responder para responder o proximo desafio,")
                    Console.WriteLine("/placar para visualizar o placar, ")
                    Console.WriteLine("/players  para visualizar os jogadores")
                    Console.WriteLine("/desistir para sair do jogo.")
                    Console.WriteLine("/placar_i para visualizar o placar indivdual")
            Case TIPO_MENU_ADM
                    exibaCabecaMenu()
                    Console.WriteLine("*******************************************************************")
                    Console.WriteLine("Você é o administrador do jogo")
                    Console.WriteLine("Digite: '/c' para começar o jogo")
                    Console.WriteLine("*******************************************************************")
                    Console.WriteLine("Outras opções")
                    exibaMenuDoJogo(TIPO_MENU_JOGO_NAO_INICIADO)
        End Select

        obtenhaEProcesseAcao()
    End Sub

    Private Function jogoPodeSerIniciado() As Boolean
        Return Not jogadorClient.getQtdJogadores < 2 And Not jogadorClient.getStatus = 0
    End Function

    Private Sub exibaCabecaMenu()
        Console.WriteLine("**********************************************************")
        Console.WriteLine("Menu Principal")
        Console.WriteLine("Escolha umas das opções abaixo:")
        Console.WriteLine("**********************************************************")
    End Sub

End Module
