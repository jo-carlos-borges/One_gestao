/**
 * Animação de chuva para a tela de login
 */
export function initRainAnimation() {
    // Configurações da animação
    const config = {
        numLines: 15,
        colors: ['#b3a4cc', '#a07fd4', '#5b7fec'],
        minSpeed: 2,
        maxSpeed: 5,
        minDelay: 0,
        maxDelay: 5,
        minOpacity: 0.1,
        maxOpacity: 0.5,
        minHeight: 10,
        maxHeight: 30
    };

    // Elemento container
    const container = document.querySelector('.animate__lines');
    if (!container) return;

    // Limpar linhas existentes
    container.innerHTML = '';

    // Criar novas linhas
    for (let i = 0; i < config.numLines; i++) {
        createRainLine(container, config);
    }
}

/**
 * Cria uma linha de chuva
 */
function createRainLine(container, config) {
    // Criar elemento
    const line = document.createElement('div');
    line.className = 'line';

    // Posição horizontal aleatória
    const leftPos = Math.random() * 100;
    line.style.left = `${leftPos}%`;

    // Altura aleatória
    const height = config.minHeight + Math.random() * (config.maxHeight - config.minHeight);
    line.style.height = `${height}vh`;

    // Velocidade aleatória
    const speed = config.minSpeed + Math.random() * (config.maxSpeed - config.minSpeed);

    // Atraso aleatório
    const delay = config.minDelay + Math.random() * (config.maxDelay - config.minDelay);

    // Opacidade aleatória
    const opacity = config.minOpacity + Math.random() * (config.maxOpacity - config.minOpacity);

    // Cor aleatória do gradiente
    const colorIndex = Math.floor(Math.random() * config.colors.length);
    const color = config.colors[colorIndex];

    // Aplicar estilos
    line.style.opacity = opacity;
    line.style.width = '1px';
    line.style.position = 'absolute';
    line.style.top = '-50vh';
    line.style.background = `linear-gradient(to bottom, rgba(179, 164, 204, 0.1), ${color})`;
    line.style.zIndex = '1';

    // Adicionar ao container
    container.appendChild(line);

    // Iniciar animação com atraso
    setTimeout(() => {
        animateLine(line, speed, container, config);
    }, delay * 1000);
}

/**
 * Anima uma linha de chuva
 */
function animateLine(line, speed, container, config) {
    let position = -50;

    // Função de animação
    function animate() {
        position += speed * 0.1;
        line.style.top = `${position}vh`;

        // Reiniciar quando sair da tela
        if (position > 150) {
            container.removeChild(line);
            createRainLine(container, config);
            return;
        }

        requestAnimationFrame(animate);
    }

    // Iniciar animação
    animate();
}