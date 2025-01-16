# XatGràfic

Este proyecto implementa un chat con interfaz gráfica, proporcionando una experiencia mejorada para la interacción de múltiples usuarios en tiempo real. Está desarrollado utilizando Swing para la interfaz gráfica y una arquitectura multithreading en el servidor.

Características principales

Funcionalidades implementadas

    Chat gráfico:
    
        Interfaz gráfica sencilla creada con Swing.
        Incluye:
            Área de entrada: Para que el usuario escriba mensajes.
            Área de mensajes: Muestra el historial de la conversación en tiempo real.
            Área de usuarios: Lista los usuarios conectados utilizando un JList.
        Uso del modelo DefaultListModel para gestionar dinámicamente los datos del JList.

    Soporte multicliente:
        Permite la conexión de múltiples usuarios simultáneamente.
        Los mensajes enviados por un usuario se retransmiten a todos los demás usuarios conectados.

Ejecución

Servidor

    Inicia el servidor en un terminal: "java ChatServer <puerto>"

    - java ChatServer 12345

Cliente

    Ejecuta el cliente gráfico: "java ChatClientGrafico <host> <puerto>"

    - java ChatClientGrafico localhost 12345

host: Dirección IP o nombre del servidor.

port: Puerto donde escucha el servidor.

