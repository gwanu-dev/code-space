package com.ssafy.algorithmstudy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    // cctv 좌표 리스트
    private static ArrayList<Position> cctvList = new ArrayList<>();
    
    // 상, 하, 좌, 우
    private static int[] dr = {    -1, 1, 0, 0 };
    private static int[] dc = { 0, 0, -1, 1 };
    // 위쪽:0, 오른쪽:1, 아래쪽:2, 왼쪽:3
    private static int[][] dir = {
            {}, // 0번 cctv는 없으므로 비워둠 (padding)
            {1},
            {1, 3},
            {0, 1},
            {0, 1, 3},
            {0, 1, 2, 3}
    };
    
    private static int N, M;
    private static int[][] office;
    
    private static int answer = 100;
    
    public static void main(String[] args) throws IOException {
        System.setIn(new FileInputStream("BJ15683i.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        office = new int[N][M];
        
//        for(int i=0; i<N; i++) {
//            office[i] = Arrays.stream(br.readLine()
//                              .split(" "))
//                              .mapToInt(Integer::parseInt)
//                              .toArray();
//        }
        
        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<M; j++) {
                // -1:감시 구역, 0:빈칸, 1~5:cctv, 6:벽
                office[i][j] = Integer.parseInt(st.nextToken());
                
                // cctv 라면,
                if(1 <= office[i][j] && office[i][j] <= 5) {
                    int num = office[i][j];
                    // cctv 위치와 번호 저장
                    cctvList.add(new Position(i, j, num));
                }
            }
        }
        
        dfs(0, office);
        System.out.println(answer);
    }
    
    /**
     * dfs로 cctv의 감시상태를 업데이트하는 함수
     * @param cctvCount 현재 dfs 단계에서의 cctv 개수
     * @param office 현재 office의 감시 상태
     */
    private static void dfs(int cctvCount, int[][] office) {
        // 모든 cctv 체크를 다했다면,
        if(cctvCount == cctvList.size()) {
            // 사각지대의 최소값 업데이트
            answer = Math.min(answer, getBlindSpotCount(office));
            for (int k = 0; k < office.length; k++){
                for (int s = 0; s < office[0].length; s++) {
                    System.out.print(office[k][s]);
                }
                System.out.println();
            }
            return;
        }
        
        int row = cctvList.get(cctvCount).row; // 행
        int col = cctvList.get(cctvCount).col; // 열
        int num = cctvList.get(cctvCount).num; // cctv의 번호
        
        // 90도씩 4번 모두 체크 (0, 90, 180, 270)
        for(int d=0; d<4; d++) {
            int[][] copiedOffice = getDeepCopiedOffice(office);
            
            // num번 cctv의 이동방향
            // 위쪽:0, 오른쪽:1, 아래쪽:2, 왼쪽:3
            for(int move : dir[num]) {
                int nd = (move + d) % 4; // 4번째(360도)는 0번째(0도)와 같기 때문에 %4로 0을 만들어줌
                int nr = row;
                int nc = col;
                
                while(true) {
                    nr += dr[nd]; // 회전했을 때의 방향에 따른 row값 한칸씩 이동
                    nc += dc[nd]; // 회전했을 때의 방향에 따른 col값 한칸씩 이동
                    
                    // 이동한 곳이 접근할 수 없거나 벽인 경우
                    if(isValidIndex(nr, nc) == false) {
                        break;
                    }
                    
                    // 감시 가능한 곳은 -1로 체크
                    copiedOffice[nr][nc] = -1;
                }
            }
            
            // 한 개의 cctv 감시 구역 체크 완료했으므로 cctvCount + 1,
            // 감시 체크한 깊은 복사한 오피스 인자로 넘겨줌
            dfs(cctvCount + 1, copiedOffice);
        }
    }
    
    /**
     * 사각지대의 칸 수를 얻는 함수
     * @param copiedOffice 복사된 오피스 상태
     * @return blindSpotCount : Integer
     */
    private static int getBlindSpotCount(int[][] copiedOffice) {
        int blindSpotCount = 0;
        
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(copiedOffice[i][j] == 0) {
                    blindSpotCount++;
                }
            }
        }
        
        return blindSpotCount;
    }
    
    /**
     * 사무실 상태를 깊은 복사해서 반환하는 함수
     * @param office 사무실 상태
     * @return deepCopiedOffice : int[][]
     */
    private static int[][] getDeepCopiedOffice(int[][] office) {
        int[][] deepCopiedOffice = new int[N][M];
        
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                deepCopiedOffice[i][j] = office[i][j];
            }
        }
        
        return deepCopiedOffice;
    }
    
    /**
     * 행과 열을 인자로 받아, 접근할 수 있는 유효한 인덱스인지 판단해서 boolean 값을 반환하는 함수
     * @param row 행
     * @param col 열
     * @return isValid : Boolean
     */
    private static boolean isValidIndex(int row, int col) {
        if(row < 0 || row >= N) {
            return false;
        }
        if(col < 0 || col >= M) {
            return false;
        }
        // 벽
        if(office[row][col] == 6) {
            return false;
        }
        
        return true;
    }
}

/**
 * CCTV의 행, 열, 번호를 저장하는 클래스
 */
class Position {
    int row;
    int col;
    int num;
    
    public Position(int row, int col, int num) {
        this.row = row;
        this.col = col;
        this.num = num;
    }
}