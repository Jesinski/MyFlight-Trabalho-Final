package pucrs.myflight.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jxmapviewer.viewer.GeoPosition;

import java.awt.Color;

import pucrs.myflight.modelo.*;

public class GerenciadorConsultas {

    private static GerenciadorConsultas instance;
    private static GerenciadorRotas gerRotas;

    public GerenciadorConsultas() {
    }

    public static GerenciadorConsultas getInstance() {
        if (instance == null) {
            instance = new GerenciadorConsultas();
        }
        return instance;
    }

    public void consultaExemplo(GerenciadorMapa gerMapa) {
        // Lista para armazenar o resultado da consulta
        List<MyWaypoint> lstPoints = new ArrayList<>();

        Aeroporto poa = new Aeroporto("POA", "Salgado Filho", new Geo(-29.9939, -51.1711));
        Aeroporto gru = new Aeroporto("GRU", "Guarulhos", new Geo(-23.4356, -46.4731));
        Aeroporto lis = new Aeroporto("LIS", "Lisbon", new Geo(38.772, -9.1342));
        Aeroporto mia = new Aeroporto("MIA", "Miami International", new Geo(25.7933, -80.2906));

        gerMapa.clear();
        Tracado tr = new Tracado();
        tr.setLabel("Teste");
        tr.setWidth(5);
        tr.setCor(new Color(0, 0, 0, 60));
        tr.addPonto(poa.getLocal());
        tr.addPonto(mia.getLocal());

        gerMapa.addTracado(tr);

        Tracado tr2 = new Tracado();
        tr2.setWidth(5);
        tr2.setCor(Color.BLUE);
        tr2.addPonto(gru.getLocal());
        tr2.addPonto(lis.getLocal());
        gerMapa.addTracado(tr2);

        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints

        lstPoints.add(new MyWaypoint(Color.RED, poa.getCodigo(), poa.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, gru.getCodigo(), gru.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, lis.getCodigo(), lis.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, mia.getCodigo(), mia.getLocal(), 5));

        // Para obter um ponto clicado no mapa, usar como segue:
        // GeoPosition pos = gerenciador.getPosicao();

        // Informa o resultado para o gerenciador
        // gerMapa.setPontos(lstPoints);

        // Quando for o caso de limpar os traçados...
        // gerenciador.clear();

        gerMapa.getMapKit().repaint();
    }

    public void consulta1(GerenciadorMapa gerMapa, GerenciadorAeroportos gerAero) {
        gerMapa.clear();

        ArrayList<Rota> rotas = GerenciadorRotas.getInstance().listarTodas();

        for (Rota r : rotas) {
            Tracado tr2 = new Tracado();
            tr2.setWidth(5);
            tr2.setCor(new Color(0, 0, 0, 60));
            tr2.addPonto(r.getOrigem().getLocal());
            tr2.addPonto(r.getDestino().getLocal());
            gerMapa.addTracado(tr2);
        }

        List<MyWaypoint> lstPoints = new ArrayList<>();
        for (Aeroporto a : gerAero.listarTodos()) {
            lstPoints.add(new MyWaypoint(Color.GREEN, a.getCodigo(), a.getLocal(), 5));
        }
        gerMapa.setPontos(lstPoints);

        Set<Aeroporto> setAeroporto = new HashSet<Aeroporto>();

        gerMapa.getMapKit().repaint();
    }

    public void plotarAeroPorCia(GerenciadorMapa gerMapa, ArrayList<Rota> rotasDaCia) {
        gerMapa.clear();

        HashSet<Aeroporto> aeroportosCiaOpera = new HashSet<Aeroporto>();

        List<MyWaypoint> lstPoints = new ArrayList<>();

        for (Rota r : rotasDaCia) {
            if (!aeroportosCiaOpera.contains(r.getOrigem())) {
                aeroportosCiaOpera.add(r.getOrigem());
            }
            if (!aeroportosCiaOpera.contains(r.getDestino())) {
                aeroportosCiaOpera.add(r.getDestino());
            }
            Tracado tr2 = new Tracado();
            tr2.setWidth(1);
            tr2.setCor(Color.BLUE);
            tr2.addPonto(r.getOrigem().getLocal());
            tr2.addPonto(r.getDestino().getLocal());
            gerMapa.addTracado(tr2);
        }
        for (Aeroporto a : aeroportosCiaOpera) {
            lstPoints.add(new MyWaypoint(Color.GREEN, a.getCodigo(), a.getLocal(), 5));
        }

        gerMapa.setPontos(lstPoints);
        gerMapa.getMapKit().repaint();
    }

    public void setTraffic(GerenciadorMapa gerMapa, GerenciadorAeroportos gerAero, HashMap<String, Integer> traffic) {
        gerMapa.clear();

        List<MyWaypoint> lstPoints = new ArrayList<>();

        for (String s : traffic.keySet()) {
            Aeroporto temp = gerAero.buscarCodigo(s);
            int tamPonto = traffic.get(s) / 10;
            if (tamPonto < 50) {
                lstPoints.add(new MyWaypoint(Color.GREEN, temp.getCodigo(), temp.getLocal(), tamPonto));
            } else {
                lstPoints.add(new MyWaypoint(Color.RED, temp.getCodigo(), temp.getLocal(), tamPonto));
            }
        }

        gerMapa.setPontos(lstPoints);
        gerMapa.getMapKit().repaint();
    }

    public void mostarEsseAeroporto(GerenciadorMapa gerMapa, Aeroporto esseAeroporto) {
        gerMapa.clear();
        List<MyWaypoint> lista = new ArrayList<MyWaypoint>();
        lista.add(new MyWaypoint(Color.CYAN, esseAeroporto.getCodigo(), esseAeroporto.getLocal(), 10));
        gerMapa.setPontos(lista);
        gerMapa.getMapKit().repaint();
    }

    public Aeroporto getAirportFromCoord(GeoPosition pos) {
        double latitude = pos.getLatitude();
        double longitude = pos.getLongitude();
        Geo posEmGeo = new Geo(latitude, longitude);
        GerenciadorAeroportos gerAero = GerenciadorAeroportos.getInstance();
        Aeroporto fetched = gerAero.getAirportFromGPS(posEmGeo);
        return fetched;

    }

    public void consulta4(Double tempoMax, GerenciadorMapa gerMapa, GerenciadorAeroportos gerAero,
            GerenciadorRotas gerRotoas, Aeroporto aero) {
        double tempo = 0;
        double dist = 0;
        for (Rota r : gerRotoas.listarTodas()) {
            if (r.getOrigem() == aero) {
                dist = aero.getLocal().distancia(r.getDestino().getLocal());
                tempo = (dist / 805) + 1;
                if (tempo <= tempoMax) {
                    Tracado tr2 = new Tracado();
                    tr2.setWidth(1);
                    tr2.setCor(Color.BLUE);
                    tr2.addPonto(r.getOrigem().getLocal());
                    tr2.addPonto(r.getDestino().getLocal());
                    gerMapa.addTracado(tr2);
                }
            }
        }
    }

    public void consulta3(GerenciadorRotas gerRotas, String origem, String destino) {
        String aux = origem;
        String aux1 = destino;
        ArrayList<String> duasConex;
        duasConex = gerRotas.acharRotaComDuasConexoes(origem, destino);
        ArrayList<String> umaConex = gerRotas.acharRotaComUmaConexao(aux, aux1);
        for (String string : umaConex) {
            System.out.println("uma conex--" + string);
        }
        for (String string : duasConex) {
            System.out.println("duas conex--" + string);
        }

    }

}
