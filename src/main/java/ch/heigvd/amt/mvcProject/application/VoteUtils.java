package ch.heigvd.amt.mvcProject.application;

public class VoteUtils {

    // Vote values
    public static final int UPVOTE      =  1;
    public static final int NOVOTE      =  0;
    public static final int DOWNVOTE    = -1;

    /**
     * Get the vote value when voting on a quesiton
     * @param startVoteValue : start vote value of the user (if he already voted on the question)
     * @param voteValue : vote value of the current vote
     * @return the new vote value
     */
    public static int getNewVoteValue(int startVoteValue, int voteValue) {
        int result = NOVOTE;

        if(voteValue != startVoteValue){
            result = voteValue;
        }

        return result;
    }

}
