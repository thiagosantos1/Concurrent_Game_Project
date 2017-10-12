Imports System.Net.Sockets
Imports System.IO
Imports System.Text
Imports System.Threading

Module JogadorCliente

    Private clienteSocket As TcpClient
    Private serverStream As NetworkStream
    Private clientStream As StreamWriter
    Private msgRetorno As String
    Private msgEnvio As String

    Sub Main()
        Try
            inicializaVariaveis()

            While Not String.IsNullOrEmpty(obtenhaMsgRetorno())
                traduzaMensagemDeRespostaExecuteAcao(msgRetorno)
            End While

        Catch ex As Exception
            Console.Write(ex.Message)
        End Try

    End Sub

    Private Function obtenhaMsgRetorno() As String
        Thread.Sleep(3000)
        Dim bytesRetorno(clienteSocket.ReceiveBufferSize) As Byte
        serverStream.Read(bytesRetorno, 0, CInt(clienteSocket.ReceiveBufferSize))
        msgRetorno = System.Text.Encoding.ASCII.GetString(bytesRetorno)
        Return msgRetorno
    End Function

    Private Sub enviarMensage(ByVal mensagem As String)
        clientStream.WriteLine(mensagem)
        Thread.Sleep(3000)
    End Sub

    Private Sub inicializaVariaveis()
        clienteSocket = New TcpClient()
        clienteSocket.Connect("localhost", 6789)  'conctando no servidor
        clientStream = New StreamWriter(clienteSocket.GetStream()) 'variavél utilizada para enviar bytes ao servidor
        clientStream.AutoFlush = True
        serverStream = clienteSocket.GetStream()  'variável utilizada para receber informações do servidor
    End Sub

    Private Sub encerreConexoes()
        serverStream.Close()
        clientStream.Close()
    End Sub

    Private Sub traduzaMensagemEExecuteAcaoSEparada(ByVal mensagem As String)
        If mensagem.Contains("recusado_iniciou") Then

            Console.WriteLine("**********************************************************")
            Console.WriteLine("Conexao recusada! Jogo ja iniciado. Aguarde o proximo")
            Console.WriteLine("**********************************************************")
            encerreConexoes()
        End If


        If mensagem.Contains("recusado_full") Then

            Console.WriteLine("*******************************************************************")
            Console.WriteLine("Conexao recusada! Jogo ja possui 5 jogadores, Aguarde o proximo")
            Console.WriteLine("*******************************************************************")
            encerreConexoes()
        End If

        If mensagem.Contains("conectado") Then

            Console.WriteLine("******************************************************************")
            Console.WriteLine("     Bem vindo ao Tradutor Italino")
            Console.WriteLine("     Numero de jogadores necessarios: 2 a 5")
            Console.WriteLine("******************************************************************")
        End If
        If mensagem.Contains("nome:") Then
            Console.WriteLine("Digite seu nome :")
            msgEnvio = Console.ReadLine()
            enviarMensage(msgEnvio)
            obtenhaMsgRetorno()
            traduzaMensagemDeRespostaExecuteAcao(msgRetorno)
        End If

        If mensagem.Contains("faltanto_jogadores") Then

            Console.WriteLine("O jogo nao pode ser ainda inicializado, e necessario no minimo 2 e")
            Console.WriteLine("no maximo 5 jogadores.Aguarde os proximos jogadores por favor")
            Console.WriteLine("******************************************************************")
            Console.WriteLine("Menu Principal")
            Console.WriteLine("Digite '/players' para listar os jogadores conectados ao jogo,")
            Console.WriteLine("'/regras' para listar as regras do jogo ou,")
            Console.WriteLine("'/desistir' para desistir do jogo")
            Console.WriteLine("******************************************************************")
        End If

        If mensagem.Contains("opcao") Then
            Console.WriteLine("Digite opcao: ")
            msgEnvio = Console.ReadLine()
            enviarMensage(msgEnvio)
            obtenhaMsgRetorno()
            traduzaMensagemDeRespostaExecuteAcao(msgRetorno)
        End If

        If mensagem.Contains("jogadores_ready") Then

            Console.WriteLine("O jogo ainda nao comecou, porem ja se tem no minimo 2 jogadores conectados")
            Console.WriteLine("******************************************************************")
            Console.WriteLine("Menu Principal")
            Console.WriteLine("Digite '/iniciar' para comecar, ")
            Console.WriteLine("/players' para listar os jogadores, ")
            Console.WriteLine("'/regras' para listar as regras do jogo ou,")
            Console.WriteLine("'/desistir' para desistir do jogo")
            Console.WriteLine("******************************************************************")

        End If

        If mensagem.Contains("opcao") Then
            Console.WriteLine("Digite opcao: ")
            msgEnvio = Console.ReadLine()
            enviarMensage(msgEnvio)
            obtenhaMsgRetorno()
            traduzaMensagemDeRespostaExecuteAcao(msgRetorno)
        End If

        If mensagem.Contains("players") Then

            Console.WriteLine("**********************************")
            Console.WriteLine("Jogadores Conectados")
            Console.WriteLine("**********************************")
        End If

        If mensagem.Contains("jogadores") Then

        End If

        If mensagem.Contains("regras") Then

            Console.WriteLine("**********************************************************************************")
            Console.WriteLine("O jogo tem uma duracao de 3 minutos, e voce comeÃ§a com 25 pontos.")
            Console.WriteLine("Para cada palavra acertada, voce ganha 1 ponto, e mais 1 ponto de cada um dos outros")
            Console.WriteLine("jogadores. A cada erro, voce perde 1 ponto. Caso sua pontuaÃ§Ã£o chege a 0, voce")
            Console.WriteLine("Ã© eliminado do jogo. No final, ganha quem fizer mais pontos.")
            Console.WriteLine("**********************************************************************************")
        End If

        If mensagem.Contains("jogo_iniciado") Then

            Console.WriteLine("O jogo Tradutor comeÃ§ou!")
            Console.WriteLine("Boa Sorte!")
        End If

        If mensagem.Contains("desistir") Then

            Console.WriteLine("*****************************************")
            Console.WriteLine("Obrigado pela sua participaÃ§Ã£o Jogo Desconectado")
            Console.WriteLine("*****************************************")
        End If
        If mensagem.Contains("invalido") Then

            Console.WriteLine("******************************")
            Console.WriteLine("Opcao invalida")
            Console.WriteLine("******************************")
        End If
        If mensagem.Contains("desafio") Then

            Console.WriteLine(mensagem + ": Significa ?")
        End If

        If mensagem.Contains("pontuacao") Then

            Console.WriteLine("PontuaÃ§Ã£o: " + mensagem.Substring(10, (mensagem.Length - 10)))
        End If
        If mensagem.Contains("menuJogo") Then

            Console.WriteLine("**********************************************************")
            Console.WriteLine("Escolha umas das opcÃµes abaixo")
            Console.WriteLine("**********************************************************")
            Console.WriteLine("/responder")
            Console.WriteLine("/placar")
            Console.WriteLine("/players")
            Console.WriteLine("/desistir")
        End If
        If mensagem.Contains("responder") Then

            Console.WriteLine("Resposta: ")
            enviarMensage(Console.ReadLine)
            obtenhaMsgRetorno()
            traduzaMensagemDeRespostaExecuteAcao(msgRetorno)
        End If
        If mensagem.Contains("acertou") Then

            Console.WriteLine("***************************")
            Console.WriteLine("     Acertou, Braaavo!!!!")
            Console.WriteLine("*************************** ")
        End If
        If mensagem.Contains("placar") Then

            Console.WriteLine("Placar do jogo : " & obtenhaMsgRetorno() & "-  " & obtenhaMsgRetorno())
        End If
        If mensagem.Contains("errou") Then

            Console.WriteLine("errou, essa nao e a traducao correta!")
        End If
        If mensagem.Contains("fim_rodada") Then

            Console.WriteLine("A palavra " & obtenhaMsgRetorno() & " foi traduzida", _
             " Vamos para a proxima!!!")
        End If
        If mensagem.Contains("ganhou") Then

            Console.WriteLine("**********************************************************")
            Console.WriteLine(" ParabÃ©ns, vocÃª foi o ganhador!!!")
            Console.WriteLine("**********************************************************")
        End If
        If mensagem.Contains("game_over") Then

            Console.WriteLine("****************************************************************")
            Console.WriteLine("Fim de jogo, o jogador " & obtenhaMsgRetorno() & " Ganho o jogo!!!!")
            Console.WriteLine("****************************************************************")
        End If
        If mensagem.Contains("perdeu") Then

            Console.WriteLine("**********************************************************")
            Console.WriteLine(" Game Over, vocÃª foi eliminado!!!")
            Console.WriteLine("**********************************************************")
        End If
        If mensagem.Contains("jogador_eliminado") Then

            Console.WriteLine("**********************************************************")
            Console.WriteLine("O jogador " & obtenhaMsgRetorno() & " foi eliminado!!!")
            Console.WriteLine("**********************************************************")
        End If
        If mensagem.Contains("O jogo Tradutor") Then
            Console.WriteLine(mensagem)
        End If

    End Sub

    Private Sub traduzaMensagemDeRespostaExecuteAcao(ByVal mensagemServidor As String)
        Dim MensagemSeparada As String()
        MensagemSeparada = mensagemServidor.Trim().Split("/")
        For Each mensagem As String In MensagemSeparada
            If Not String.IsNullOrEmpty(mensagem) Then
                traduzaMensagemEExecuteAcaoSEparada(mensagem)
            End If
        Next
    End Sub

End Module
