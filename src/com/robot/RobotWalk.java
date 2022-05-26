package com.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

enum Direction {
    NORTH("WR", "EL", "NM", 'N'),
    SOUTH("WL", "ER", "SM", 'S'),
    EAST("NR", "SL", "EM", 'E'),
    WEST("NL", "SR", "WM", 'W');

    private static Map<Character, Direction> directionToObjectMap;

    static {
        directionToObjectMap = Arrays.stream(Direction.values()).collect(Collectors.toMap(d -> d.direction, d -> d));
    }

    private List<String> list = new ArrayList<>(3);
    private char direction;

    Direction(String a, String b, String c, char direction) {
        list.add(a);
        list.add(b);
        list.add(c);
        this.direction = direction;
    }

    public static Direction getDirection(char initialDirection, char turn) {
        char[] array = new char[]{initialDirection, turn};
        for (Direction value : values()) {
            if (value.list.contains(new String(array))) {
                return value;
            }
        }
        return null;
    }

    public static Direction getDirection(char direction) {
        Direction dir = directionToObjectMap.get(direction);
        if (dir != null) {
            return dir;
        }
        return null;
    }

    public char getDir() {
        return direction;
    }
}

public class RobotWalk {

    static Set<Position> visitedPositions = new HashSet<>();
    static int M;
    static int N;

    public static void main(String[] args) {
        // robot movement position with x,y and NSEW
        // command L R M
        // stops:   1. if any particle
        //          2. any co ordinate already travelled
        //          3. cmd leads to position outside rectangular plane
        // 4 4
        // 0 0 N
        // MMRMMMLMMLMMLMMMMM
        Scanner scanner = new Scanner(System.in);
        M = scanner.nextInt();
        N = scanner.nextInt();
        int initx = scanner.nextInt();
        int inity = scanner.nextInt();
        char initDirection = scanner.next().charAt(0);
        String cmd = scanner.next();
        //"MMMRMMLM";
        //"MMMRMMRLM";


        Position currPosition = new Position(initx, inity, Direction.getDirection(initDirection));
        char[] command = cmd.toCharArray();
        for (int i = 0; i < command.length; i++) {
            Position position = traverse(command, currPosition.getX(), currPosition.getY(), currPosition.getDirection().getDir(), i);
            if (position != null) {
                currPosition = position;
                continue;
            } else {
                break;
            }
        }
        System.out.println(currPosition.getX() + " " + currPosition.getY() + " " + currPosition.getDirection().getDir());
    }

    static Position traverse(char[] command, int row, int col, char prev, int next) {
        Position mayBeVisited = new Position(row, col);
        if (command.length == 0 || visitedPositions.contains(mayBeVisited)) {
            return null;
        }
        Direction direction = Direction.getDirection(prev, command[next]);
        if (direction == null) {
            throw new RuntimeException("Invalid Command: " + command[next]);
        }
        switch (direction) {
            case NORTH:
                row++;
                break;
            case SOUTH:
                row--;
                break;
            case EAST:
                col++;
                break;
            case WEST:
                col--;
                break;
            default:
                return null;
        }
        if (row < 0 || row > M || col < 0 || col > N)
            return null;
        return command[next] == 'M' ? movedPosition(new Position(row, col, direction), mayBeVisited) : nonMovedPosition(mayBeVisited, direction);
    }

    static Position movedPosition(Position pos, Position visitedNow) {
        visitedPositions.add(visitedNow);
        return pos;
    }

    static Position nonMovedPosition(Position notMovedPosition, Direction dir) {
        notMovedPosition.setDirection(dir);
        return notMovedPosition;
    }
}

class Position {
    private int x;
    private int y;
    private Direction direction;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
