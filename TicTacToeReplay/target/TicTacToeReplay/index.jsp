<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="hu.deniszbecskei.dao.PartyDAOImpl" %>
<%@ page import="hu.deniszbecskei.dao.Party" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<html>
<head>
    <title>TicTacToe Replay</title>
    <link rel="stylesheet" type="text/css" href="webresources/main.css">
</head>
<body>

<img class="logo" src="https://i.imgur.com/3OpGL57.png" alt="logo">

<%!
    private String generateRow(String id, String date, int index) {
        String div_class;
        if (index % 2 == 0) {
            div_class = "class='divBaseHome divLight'";
        } else {
            div_class = "class='divBaseHome divDark'";
        }
        String p_class = "class='paragraphHome'";
        String btn_class = "class='btnHome'";
        String btnDel_class = "class='btnDel'";
        String formStart = "<form action='playoff.jsp' method='POST' "+div_class+">";
        String idParagraph = "<p>" + id + "</p>";
        String dateParagraph = "<p " + p_class + ">" + date + "</p>";
        String idHidden = "<input class='noninterference' type='hidden' value='" + id + "' name='id'>";
        String btnRedir = "<input " + btn_class + " type='submit' name='submit' value='Újrajátszás'>";
        String btnDelete = "<input " + btnDel_class + " type='submit' name='submit' value='Törlés'>";
        return "<div "+div_class+">" + formStart + idParagraph + " " + dateParagraph + " " + idHidden + " " + btnRedir + " " + "</form><form action='index.jsp' method='GET'>"+ idHidden + " " + btnDelete +"</form></div>";
    }
%>

<%
    try {
        Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    final PartyDAOImpl dao = new PartyDAOImpl();
    final List<Party> parties = dao.getParties();

    if (request.getParameter("submit") != null) {
        dao.delete(request.getParameter("id"));
        response.sendRedirect("index.jsp");
    }

    int index = 0;
    for (Party party : parties) {
        out.print(generateRow(party.getId(), party.getPlayedOn().toString(), index));
        index++;
    }
%>
</body>
</html>