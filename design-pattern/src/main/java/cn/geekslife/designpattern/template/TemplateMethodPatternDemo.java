package cn.geekslife.designpattern.template;

/**
 * 模板方法模式演示类
 */
public class TemplateMethodPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 模板方法模式演示 ===");
        
        System.out.println("\n1. 游戏模板演示:");
        // 国际象棋游戏
        System.out.println("--- 国际象棋游戏 ---");
        Game chess = new Chess();
        chess.play();
        
        // 纸牌游戏
        System.out.println("\n--- 纸牌游戏 ---");
        Game poker = new Poker();
        poker.play();
        
        // 俄罗斯方块游戏
        System.out.println("\n--- 俄罗斯方块游戏 ---");
        Game tetris = new Tetris();
        tetris.play();
        
        System.out.println("\n2. 数据库操作模板演示:");
        // MySQL数据库操作
        System.out.println("--- MySQL数据库操作 ---");
        DatabaseTemplate mysql = new MySQLDatabase();
        mysql.executeDatabaseOperation();
        
        // PostgreSQL数据库操作
        System.out.println("\n--- PostgreSQL数据库操作 ---");
        DatabaseTemplate postgresql = new PostgreSQLDatabase();
        postgresql.executeDatabaseOperation();
        
        // MongoDB数据库操作
        System.out.println("\n--- MongoDB数据库操作 ---");
        DatabaseTemplate mongodb = new MongoDBDatabase();
        mongodb.executeDatabaseOperation();
        
        System.out.println("\n3. 文件处理模板演示:");
        // XML文件处理
        System.out.println("--- XML文件处理 ---");
        FileProcessor xmlProcessor = new XMLFileProcessor();
        xmlProcessor.processFile("users.xml");
        
        // CSV文件处理
        System.out.println("\n--- CSV文件处理 ---");
        FileProcessor csvProcessor = new CSVFileProcessor();
        csvProcessor.processFile("data.csv");
        
        // JSON文件处理
        System.out.println("\n--- JSON文件处理 ---");
        FileProcessor jsonProcessor = new JSONFileProcessor();
        jsonProcessor.processFile("config.json");
    }
}