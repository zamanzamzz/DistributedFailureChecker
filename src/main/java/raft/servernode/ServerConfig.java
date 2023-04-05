package raft.servernode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    public final String nodeId;
    public final String host;
    public final List<ServerConfig> otherServerNodes;

    // Persistent State
    public final Integer currentTerm;
    public final String votedFor;
    public final List<LogEntry> log;

    public ServerConfig(String nodeId, String host, List<ServerConfig> otherServerNodes) {
        this.nodeId = nodeId;
        this.host = host;
        this.otherServerNodes = otherServerNodes;
        currentTerm = null;
        votedFor = null;
        log = new ArrayList<LogEntry>();
    }

    public ServerConfig(String nodeId, String host, List<ServerConfig> otherServerNodes, Integer currentTerm,
            String votedFor, List<LogEntry> log) {
        this.nodeId = nodeId;
        this.host = host;
        this.otherServerNodes = otherServerNodes;

        this.currentTerm = currentTerm;
        this.votedFor = votedFor;
        this.log = log;
    }

    public ServerConfig(Path jsonFilePath) throws IOException, JSONException {
        String content = new String(Files.readAllBytes(jsonFilePath));
        JSONObject jsonObject = new JSONObject(content);
        nodeId = jsonObject.getString("nodeId");
        host = jsonObject.getString("host");
        currentTerm = jsonObject.optInt("currentTerm");
        votedFor = jsonObject.optString("votedFor");
        otherServerNodes = new ArrayList<ServerConfig>();
        JSONArray otherConfigs = jsonObject.getJSONArray("otherServerNodes");
        for (Object otherConfig : otherConfigs) {
            JSONObject otherConfigJson = (JSONObject) otherConfig;
            ServerConfig serverConfig = new ServerConfig(otherConfigJson.getString("nodeId"),
                    otherConfigJson.getString("host"), new ArrayList<>());
            otherServerNodes.add(serverConfig);
        }
        log = new ArrayList<LogEntry>();
    }
}
