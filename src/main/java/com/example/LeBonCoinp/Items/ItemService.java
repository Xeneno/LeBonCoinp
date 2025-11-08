package com.example.LeBonCoinp.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.LeBonCoinp.Exceptions.ResourceNotFoundException;
import com.example.LeBonCoinp.Exceptions.ForbiddenOperationException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper mapper; 

    public ItemService(ItemRepository itemRepository, ItemMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper= mapper;
    }

    @Transactional
    public ItemResponse createItem(ItemCreateRequest req, Long sellerId) {
        Item item = new Item();
        item.setTitle(req.title());
        item.setDescription(req.description());
        item.setPrice(req.price());
        item.setImageUrl(req.imageUrl());
        item.setIsAvailable(true);
        item.setCreatedAt(java.sql.Timestamp.from(java.time.Instant.now()));
        item.setSellerId(sellerId);
        itemRepository.save(item);
        return mapper.toDto(item);
        
    }



    @Transactional(readOnly = true)
    public List<ItemResponse> getAvailableItems() {
        return itemRepository.findByIsAvailableTrue()
                .stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));
        return mapper.toDto(item);
    }

    @Transactional
    public void markAsSold(Long id, Long currentUserId) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));

        if (!item.getSellerId().equals(currentUserId)) {
            throw new ForbiddenOperationException("Not allowed to modify this item");
        }

        if (!Boolean.TRUE.equals(item.getIsAvailable())) return; 
        item.setIsAvailable(false);
        // save not required because of @Transactional 
    }

       @Transactional(readOnly = true)
    public Page<ItemResponse> getMyItems(Long sellerId, Pageable pageable) {
        Pageable effective = pageable.getSort().isUnsorted()
            ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                             Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")))
            : pageable;
        return itemRepository.findBySellerId(sellerId, effective).map(mapper::toDto);
    }

   
}
