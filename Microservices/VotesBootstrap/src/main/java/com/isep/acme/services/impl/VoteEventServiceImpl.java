package com.isep.acme.services.impl;

import com.isep.acme.model.EventTypeEnum;
import com.isep.acme.model.VoteEvent;
import com.isep.acme.repository.VoteEventRepo;
import com.isep.acme.services.interfaces.VoteEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class VoteEventServiceImpl implements VoteEventService {

    @Autowired
    private VoteEventRepo voteEventRepo;

    @Override
    public List<VoteEvent> getAllVotes() {
        return voteEventRepo.findAll();
    }

    @Override
    public VoteEvent addVoteEvent(VoteEvent voteEvent) {
        boolean flag = false;
        for (VoteEvent v : getAllVotes()) {
            if (v.getVoteID() == voteEvent.getVoteID() && v.getEventTypeEnum() == EventTypeEnum.DELETE) {
                flag = true;
                break;
            }
        }
        if (flag) {
            throw new RuntimeException("Vote does not exists");
        }
        return voteEventRepo.save(voteEvent);
    }
}