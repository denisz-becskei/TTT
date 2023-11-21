<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="hu.alkfejl.dao.Party" %>
<%@ page import="hu.alkfejl.dao.PartyDAO" %>
<%@ page import="hu.alkfejl.dao.PartyDAOImpl" %>
<%@ page import="hu.alkfejl.dao.Coords" %>
<%@ page import="hu.alkfejl.Control" %>
<%@ page %><%--
  Created by IntelliJ IDEA.
  User: Gustyff
  Date: 4/29/2021
  Time: 6:03 PM
--%>
<html>
<head>
    <title>TicTacToe Replay</title>
    <link rel="stylesheet" type="text/css" href="webresources/main.css">
    <link rel="stylesheet" type="text/css" href="webresources/playoff.css">
</head>
<body>
<%!
    private static int currentMove;
    private static int maxMoves;
    private static int cellSizes;

    private final PartyDAO dao = new PartyDAOImpl();

    private Party getBoard(String id) {
        return dao.getParty(id);
    }

    private static int fieldSize;

    private String[][] generate_fields() {
        String[][] strings = new String[fieldSize][fieldSize];
        cellSizes = (int) Math.round(500.0 / fieldSize);

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                int leftPos = 75 + j * (int) Math.round(600.0 / fieldSize);
                int topPos = 180 + i * (int) Math.round(600.0 / fieldSize);
                strings[i][j] = "<div class='cell' style='left:" + leftPos + "px; top: " + topPos + "px; width: "+cellSizes+"px; height: "+cellSizes+"px;'></div>";
            }
        }
        return strings;
    }

    private String generateAutomaticMoveInputs() {
        return "<input class='automataTimeInput' placeholder='Automata lépésideje (mp)' type='text' name='automaticSec'>" + "<br>" +
                "<input class='btnReplay btnReplayAutomata' type='submit' value='Automata indítása' name='automaticStart'>";
    }

    private String[][] fields;
%>

<%
    try {
        Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

    if (request.getParameter("id") == null) {
        response.sendRedirect("index.jsp");
    }

    %>
<a href='index.jsp' class='backBtn'>Vissza</a>
<%
    String matchId = request.getParameter("id");
    Party party = getBoard(matchId);

    fieldSize = party.getTableSize();
    if (request.getParameter("submit") != null) {
        currentMove = 0;
        maxMoves = party.getNumberOfMoves();
        fields = generate_fields();
    }

    if (request.getParameter("fwd") != null || request.getParameter("automaticStart") != null) {
        currentMove++;
        if (currentMove > maxMoves) {
            currentMove = maxMoves;
        }
    }
    if (request.getParameter("bwd") != null) {
        if (currentMove > 0) {
            currentMove--;
        }
    }
    if (request.getParameter("submit") == null) {
        if (request.getParameter("fwd") != null) {
            Coords nextMove = party.getMove(currentMove);
            if (nextMove != null) {
                if (currentMove % 2 == 0) {
                    fields[nextMove.getX()][nextMove.getY()] = "<div class='cell limeCell' style='left:" + (75 + nextMove.getY() * (int) Math.round(600.0 / fieldSize)) + "px; top: " + (180 + nextMove.getX() * (int) Math.round(600.0 / fieldSize)) + "px; width: "+cellSizes+"; height: "+cellSizes+"'></div>";
                } else {
                    fields[nextMove.getX()][nextMove.getY()] = "<div class='cell redCell' style='left:" + (75 + nextMove.getY() * (int) Math.round(600.0 / fieldSize)) + "px; top: " + (180 + nextMove.getX() * (int) Math.round(600.0 / fieldSize)) + "px; width: "+cellSizes+"; height: "+cellSizes+"'></div>";
                }
            }
        }
        else if (request.getParameter("bwd") != null) {
            Coords upperMove = party.getMove(currentMove + 1);
            fields[upperMove.getX()][upperMove.getY()] = "<div class='cell whiteCell' style='left:" + (75 + upperMove.getY() * (int) Math.round(600.0 / fieldSize)) + "px; top: " + (180 + upperMove.getX() * (int) Math.round(600.0 / fieldSize)) + "px;  width: "+cellSizes+"; height: "+cellSizes+"'></div>";
        }
        else if (request.getParameter("automaticStart") != null) {
            if(Control.isNumeric(request.getParameter("automaticSec"))) {
                Coords nextMove = party.getMove(currentMove);
                if (nextMove != null) {
                    if (currentMove % 2 == 0) {
                        fields[nextMove.getX()][nextMove.getY()] = "<div class='cell limeCell' style='left:" + (75 + nextMove.getY() * (int) Math.round(600.0 / fieldSize)) + "px; top: " + (180 + nextMove.getX() * (int) Math.round(600.0 / fieldSize)) + "px; width: "+cellSizes+"; height: "+cellSizes+"'></div>";
                    } else {
                        fields[nextMove.getX()][nextMove.getY()] = "<div class='cell redCell' style='left:" + (75 + nextMove.getY() * (int) Math.round(600.0 / fieldSize)) + "px; top: " + (180 + nextMove.getX() * (int) Math.round(600.0 / fieldSize)) + "px; width: "+cellSizes+"; height: "+cellSizes+"'></div>";
                    }
                }
                if (currentMove != maxMoves) {
                    int automataStep = Integer.parseInt(request.getParameter("automaticSec"));
                    if (automataStep < 1) {
                        response.setIntHeader("Refresh", 1);
                    } else {
                        response.setIntHeader("Refresh", Integer.parseInt(request.getParameter("automaticSec")));
                    }

                }
            } else {
                response.sendRedirect("index.jsp");
            }
        }
    }
    for (int i = 0; i < fieldSize; i++) {
        for (int j = 0; j < fieldSize; j++) {
            out.print(fields[i][j]);
        }
    }
%>


    <div class="cell redCell demoCell1"></div><p class="demoText1"> - X </p>
    <div class="cell limeCell demoCell2"></div><p class="demoText2"> - O </p>
    <form method='GET' action='playoff.jsp'>

<%  out.print("<input class='noninterference' type='hidden' value='" + request.getParameter("id") + "' name='id'>");
    out.print("<p class='paragraphReplay' type='submit' name='position'>Lépés: " + currentMove + " / " + maxMoves + "</p>");
    if (currentMove == maxMoves) {
        out.print("<input class='btnReplay btnReplayFwd' type='submit' name='fwd' value='Lépés +' disabled>");
    } else {
        out.print("<input class='btnReplay btnReplayFwd' type='submit' name='fwd' value='Lépés +'>");
    }
    if (currentMove == 0) {
        out.print("<input class='btnReplay btnReplayBwd' type='submit' name='bwd' value='Lépés -' disabled>");
    } else {
        out.print("<input class='btnReplay btnReplayBwd' type='submit' name='bwd' value='Lépés -'>");
    }
    if (request.getParameter("submit") != null) {
        out.print(generateAutomaticMoveInputs());
    }
%>
    </form>
</body>
</html>
