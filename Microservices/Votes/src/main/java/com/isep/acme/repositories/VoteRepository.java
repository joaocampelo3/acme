package com.isep.acme.repositories;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.voteID=:voteID")
    Optional<Vote> findByID(Long voteID);

    //Delete the vote when given the voteID
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.voteID=:voteID")
    void deleteByVoteID(@Param("voteID") Long voteID);

    //Update the vote when given the voteID
    @Transactional
    @Modifying
    @Query("UPDATE Vote v SET v.voteID = :voteID WHERE v.voteID=:voteID")
    Vote updateByVoteID(@Param("voteID") String voteID);

    @Query("SELECT v FROM Vote v WHERE v.voteID=:voteID")
    Optional<Vote> findById(Long voteID);

}
