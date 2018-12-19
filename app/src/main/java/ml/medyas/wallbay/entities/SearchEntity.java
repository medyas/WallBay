package ml.medyas.wallbay.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import ml.medyas.wallbay.entities.pexels.Photo;
import ml.medyas.wallbay.entities.pixabay.Hit;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import ml.medyas.wallbay.utils.Utils;

public class SearchEntity {
    private PixabayEntity pixabayEntity;
    private UnsplashSearchEntity unsplashPhotoEntity;
    private PexelsEntity pexelsEntity;

    public SearchEntity(PixabayEntity pixabayEntity, UnsplashSearchEntity unsplashPhotoEntity, PexelsEntity pexelsEntity) {
        this.pixabayEntity = pixabayEntity;
        this.unsplashPhotoEntity = unsplashPhotoEntity;
        this.pexelsEntity = pexelsEntity;
    }

    public SearchEntity() {
    }

    public PixabayEntity getPixabayEntity() {
        return pixabayEntity;
    }

    public void setPixabayEntity(PixabayEntity pixabayEntity) {
        this.pixabayEntity = pixabayEntity;
    }

    public UnsplashSearchEntity getUnsplashPhotoEntity() {
        return unsplashPhotoEntity;
    }

    public void setUnsplashPhotoEntity(UnsplashSearchEntity unsplashPhotoEntity) {
        this.unsplashPhotoEntity = unsplashPhotoEntity;
    }

    public PexelsEntity getPexelsEntity() {
        return pexelsEntity;
    }

    public void setPexelsEntity(PexelsEntity pexelsEntity) {
        this.pexelsEntity = pexelsEntity;
    }

    private List<ImageEntity> getPixabayList() {
        List<ImageEntity> list = new ArrayList<>();
        for (Hit item : pixabayEntity.getHits()) {
            ImageEntity imageEntity = new ImageEntity(
                    String.valueOf(item.getId()),
                    item.getUser(),
                    item.getUserImageURL(),
                    Utils.webSite.PIXABAY,
                    item.getLikes(),
                    item.getViews(),
                    item.getDownloads(),
                    item.getImageWidth(),
                    item.getImageHeight(),
                    item.getPageURL(),
                    item.getImageURL(),
                    item.getWebformatURL(),
                    item.getTags()
            );
            list.add(imageEntity);
        }

        return list;
    }

    private List<ImageEntity> getPexelsList() {
        List<ImageEntity> list = new ArrayList<>();
        for (Photo item : pexelsEntity.getPhotos()) {
            ImageEntity imageEntity = new ImageEntity(
                    String.valueOf(item.getId()),
                    item.getPhotographer(),
                    null,
                    Utils.webSite.PEXELS,
                    0,
                    0,
                    0,
                    item.getWidth(),
                    item.getHeight(),
                    item.getUrl(),
                    item.getSrc().getOriginal(),
                    item.getSrc().getLarge(),
                    null
            );
            list.add(imageEntity);
        }

        return list;
    }


    private List<ImageEntity> getUnsplahList() {
        List<ImageEntity> list = new ArrayList<>();
        for (UnsplashPhotoEntity item : unsplashPhotoEntity.getUnsplashPhotoEntitys()) {
            ImageEntity imageEntity = new ImageEntity(item.getId(),
                    item.getUser().getUsername(),
                    item.getUser().getProfileImage().getMedium(),
                    Utils.webSite.UNSPLASH,
                    item.getLikes(),
                    0,
                    0,
                    item.getWidth(),
                    item.getHeight(),
                    item.getLinks().getHtml(),
                    item.getLinks().getDownloadLocation(),
                    item.getUrls().getRegular(),
                    null);
            list.add(imageEntity);
        }

        return list;
    }

    public List<ImageEntity> getAll() {
        List<ImageEntity> list = new ArrayList<>();
        list.addAll(getUnsplahList());
        list.addAll(getPixabayList());
        list.addAll(getPexelsList());

        Collections.shuffle(list);
        return list;
    }
}
