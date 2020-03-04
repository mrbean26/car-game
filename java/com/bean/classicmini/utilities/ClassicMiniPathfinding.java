package com.bean.classicmini.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import glm.vec._2.Vec2;

class ClassicMiniPathfindingNode{
    public ClassicMiniPathfindingNode(ClassicMiniPathfindingNode parent, Vec2 position){
        this.parent = parent;
        this.position = position;
    }

    public ClassicMiniPathfindingNode parent = null;
    public Vec2 position = new Vec2(0.0f);

    public float gCost = 0f;
    public float hCost = 0f;
    public float fCost = 0f;

    public boolean equalNode(ClassicMiniPathfindingNode other){
        return ClassicMiniMath.vectorTwoEquals(this.position, other.position);
    }
}

public class ClassicMiniPathfinding {
    public static List<Vec2> findPath(Vec2 start, Vec2 end, Vec2[] blocks){
        ClassicMiniPathfindingNode startNode = new ClassicMiniPathfindingNode(null, start);
        ClassicMiniPathfindingNode endNode = new ClassicMiniPathfindingNode(null, end);

        List<ClassicMiniPathfindingNode> openList = new ArrayList<>();
        List<ClassicMiniPathfindingNode> closedList = new ArrayList<>();

        openList.add(startNode);
        while(openList.size() > 0){
            ClassicMiniPathfindingNode currentNode = openList.get(0);
            int currentIndex = 0;

            int openListCount = openList.size();
            for(int i = 0; i < openListCount; i++){
                ClassicMiniPathfindingNode item = openList.get(i);
                if(item.fCost < currentNode.fCost){
                    currentNode = item;
                    currentIndex = i;
                }
            }

            openList.remove(currentIndex);
            closedList.add(currentNode);

            if(currentNode.equalNode(endNode)){
                List<Vec2> path = new ArrayList<>();
                ClassicMiniPathfindingNode current = currentNode;

                while(current != null){
                    path.add(current.position);
                    current = current.parent;
                }

                Collections.reverse(path);
                return path;
            }

            List<ClassicMiniPathfindingNode> children = new ArrayList<>();
            Vec2[] allOptions = new Vec2[]{new Vec2(0f, -1f), new Vec2(0f, 1f), new Vec2(-1f, 0f), new Vec2(1f, 0f)};

            for(int i = 0; i < 4; i++){
                Vec2 newPosition = allOptions[i];
                Vec2 nodePosition = new Vec2(currentNode.position.x + newPosition.x, currentNode.position.y + newPosition.y);

                boolean continueBool = false;
                int blockCount = blocks.length;
                for(int b = 0; b < blockCount; b++){
                    if(ClassicMiniMath.vectorTwoEquals(nodePosition, blocks[b])){
                        continueBool = true;
                        break;
                    }
                }
                if(continueBool){
                    continue;
                }

                ClassicMiniPathfindingNode newNode = new ClassicMiniPathfindingNode(currentNode, nodePosition);
                children.add(newNode);
            }

            int childCount = children.size();
            for(int c = 0; c < childCount; c++){
                ClassicMiniPathfindingNode child = children.get(c);

                int closedChildCount = closedList.size();
                for(int cc = 0; cc < closedChildCount; cc++){
                    ClassicMiniPathfindingNode closedChild = closedList.get(cc);
                    if(child.equalNode(closedChild)){
                        continue; // maybe put this out of this for loop?
                    }
                }

                child.gCost = currentNode.gCost + 1f;
                child.hCost = (float) ((Math.pow((double) (child.position.x - endNode.position.x), 2.0)) + (Math.pow((double) (child.position.y - endNode.position.y), 2.0)));
                child.fCost = child.gCost + child.hCost;

                int openListCountNew = openList.size();
                for(int on = 0; on < openListCountNew; on++){
                    ClassicMiniPathfindingNode openNode = openList.get(on);
                    if(child.equalNode(openNode) && child.gCost > openNode.gCost){
                        continue; // maybe put this out of this for loop?
                    }
                }

                openList.add(child);
            }
        }
        return null;
    }
}
